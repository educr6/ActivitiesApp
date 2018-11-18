package example.com.stepsapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import example.com.stepsapp.MainActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static example.com.stepsapp.DatabaseHelper.COL1;
import static example.com.stepsapp.DatabaseHelper.COL2;
import static example.com.stepsapp.DatabaseHelper.COL3;
import static example.com.stepsapp.DatabaseHelper.COL4;
import static example.com.stepsapp.MainActivity.activityLog;


public class CurrentActivityFragment extends Fragment {

    private String currentActivityStr;
    private String startActivityTimeStr;

    private TextView currentActivityTextView;
    private TextView startTimeTextView;

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

        currentActivityStr = currentActivityTextView.getText().toString();
        startActivityTimeStr = currentActivityTextView.getText().toString();

        Button newActivityBtn = getActivity().findViewById(R.id.activity_button);
        newActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_new_activity, null);
                new AlertDialog.Builder(getContext())
                        .setTitle("New activity")
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText temp = dialogView.findViewById(R.id.activity_name_userInput);
                                currentActivityStr = temp.getText().toString();
                                currentActivityTextView.setText(currentActivityStr);

                                Calendar calendar = Calendar.getInstance();
                                final Date time = calendar.getTime();
                                startActivityTimeStr = MainActivity.printDateFormat.format(time);
                                startTimeTextView.setText(startActivityTimeStr);

                                ContentValues values = new ContentValues(2);
                                values.put(COL1, currentActivityStr);
                                values.put(COL2, MainActivity.printDateFormat.format(time));

                                LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

                                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    LocationListenerGPS listenerGPS = new LocationListenerGPS();
                                    Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    while (currentLocation == null || currentLocation.getAccuracy() > 100.) {
                                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listenerGPS);
                                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listenerGPS, null);
                                        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    }
                                    locationManager.removeUpdates(listenerGPS);
                                    values.put(COL3, currentLocation.getLatitude());
                                    values.put(COL4, currentLocation.getLongitude());

                                } else {
                                    values.put(COL3, "-1");
                                    values.put(COL4, "-1");
                                }
                                activityLog.insert(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL1, values);


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


        Cursor constantsCursor = activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
                DatabaseHelper.COL1 + ", " +
                DatabaseHelper.COL2 + ", " +
                DatabaseHelper.COL3 + ", " +
                DatabaseHelper.COL4 + " FROM " +
                DatabaseHelper.TABLE_NAME + " ORDER BY " +
                DatabaseHelper.COL2, null);
        constantsCursor.moveToLast();

        TextView activityName = getActivity().findViewById(R.id.activity_name);
        TextView startTime = getActivity().findViewById(R.id.start_time);

        activityName.setText("Activity: " + constantsCursor.getString(constantsCursor.getColumnIndex(COL1)));
        startTime.setText("Start time: " + constantsCursor.getString(constantsCursor.getColumnIndex(COL2)));
    }
}
