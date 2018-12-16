package example.com.stepsapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static example.com.stepsapp.DatabaseHelper.COL1;
import static example.com.stepsapp.DatabaseHelper.COL2;
import static example.com.stepsapp.DatabaseHelper.COL3;
import static example.com.stepsapp.DatabaseHelper.COL4;
import static example.com.stepsapp.DatabaseHelper.COL5;
import static example.com.stepsapp.DatabaseHelper.COL6;
import static example.com.stepsapp.DatabaseHelper.TABLE_NAME;
import static example.com.stepsapp.MainActivity.activityLog;
import static example.com.stepsapp.MainActivity.generalDateFormat;
import static example.com.stepsapp.MainActivity.getTimeDiff;


public class CurrentActivityFragment extends Fragment {

    private String currentActivityStr;
    private String startActivityTimeStr;

    private TextView currentActivityTextView;
    private TextView startTimeTextView;
    private TextView durationTextView;

    public CurrentActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_activity, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        currentActivityTextView = getActivity().findViewById(R.id.activity_name);
        startTimeTextView = getActivity().findViewById(R.id.start_time);
        durationTextView = getActivity().findViewById(R.id.duration);

        Button newActivityBtn = getActivity().findViewById(R.id.activity_button);
        newActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = getLayoutInflater().inflate(R.layout.alert_new_activity, null);
                final Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
                new AlertDialog.Builder(getContext())
                        .setTitle("New activity")
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText nameUserEdit = dialogView.findViewById(R.id.activity_name_userInput);


                                final Date time = Calendar.getInstance().getTime();

                                Cursor constantsCursor = MainActivity.activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
                                        COL2 + " FROM " +
                                        TABLE_NAME + " ORDER BY " +
                                        COL2, null);
                                constantsCursor.moveToLast();

                                String prevActivityStartStr = constantsCursor.getString(constantsCursor.getColumnIndex(COL2));
                                try {
                                    Date prevActivityStart = generalDateFormat.parse(prevActivityStartStr);
                                    String durationPrevActivty = getTimeDiff(time, prevActivityStart);

                                    MainActivity.activityLog.execSQL("UPDATE " + TABLE_NAME + " SET " +
                                            COL5 + " = \'" + durationPrevActivty + "\' WHERE " + COL2 + " LIKE \'" +
                                            prevActivityStartStr + "\'");
                                } catch (ParseException e){}


                                ContentValues values = new ContentValues(2);
                                values.put(COL1, nameUserEdit.getText().toString());
                                values.put(COL2, MainActivity.generalDateFormat.format(time));

                                LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                                }

                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    LocationListenerGPS listenerGPS = new LocationListenerGPS();
                                    for (int i = 0; i < 100; i++) {
//                                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listenerGPS);
                                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listenerGPS, null);
//                                        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    }
                                    locationManager.removeUpdates(listenerGPS);
                                    Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    values.put(COL3, currentLocation.getLatitude());
                                    values.put(COL4, currentLocation.getLongitude());

                                } else {
                                    Toast.makeText(getContext(), "You did not grant permission. Default location logged.", Toast.LENGTH_SHORT).show();
                                    values.put(COL3, "-1");
                                    values.put(COL4, "-1");
                                }
                                String[] stringCats = getResources().getStringArray(R.array.categories);
                                values.put(COL6, stringCats[categorySpinner.getSelectedItemPosition()]);
                                activityLog.insert(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL1, values);




                                updateFragmentView();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


            }
        });

        updateFragmentView();
    }

    private void updateFragmentView() {
        Cursor constantsCursor = activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
                DatabaseHelper.COL1 + ", " +
                DatabaseHelper.COL2 + ", " +
                DatabaseHelper.COL3 + ", " +
                DatabaseHelper.COL4 + " FROM " +
                DatabaseHelper.TABLE_NAME + " ORDER BY " +
                DatabaseHelper.COL2, null);
        constantsCursor.moveToLast();

        currentActivityTextView.setText("Activity: " + constantsCursor.getString(constantsCursor.getColumnIndex(COL1)));
        startTimeTextView.setText("Start time: " + constantsCursor.getString(constantsCursor.getColumnIndex(COL2)));
        try {
            Date currentActivityStartTime = generalDateFormat.parse(constantsCursor.getString(constantsCursor.getColumnIndex(COL2)));
            durationTextView.setText("Duration: " + MainActivity.getTimeDiff(Calendar.getInstance().getTime(), currentActivityStartTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
