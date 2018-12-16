package example.com.stepsapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static example.com.stepsapp.DatabaseHelper.COL1;
import static example.com.stepsapp.DatabaseHelper.COL2;
import static example.com.stepsapp.DatabaseHelper.COL3;
import static example.com.stepsapp.DatabaseHelper.COL4;
import static example.com.stepsapp.DatabaseHelper.COL5;
import static example.com.stepsapp.DatabaseHelper.COL6;
import static example.com.stepsapp.DatabaseHelper.TABLE_NAME;


public class ActivityHistoryFragment extends ListFragment {

    public ActivityHistoryFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Cursor constantsCursor = MainActivity.activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
                COL1 + ", " +
                COL2 + ", " +
                COL3 + ", " +
                COL4 + ", " +
                COL5 + ", " +
                COL6 + " FROM " +
                TABLE_NAME + " ORDER BY " +
                COL2, null);
        ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_activity_history, constantsCursor, new String[]
                {COL1, COL2, COL3, COL4, COL5, COL6},
                new int[]{R.id.list_activity_name, R.id.list_startTime, R.id.list_latitude, R.id.list_longtitude, R.id.list_duration, R.id.list_category}, 0);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        menu.setHeaderTitle("Activity edit");
//        String[] menuItems = {"Edit", "Delete"};
//        menu.add(Menu.NONE, 0, 0, menuItems[0]);
//        menu.add(Menu.NONE, 1, 1, menuItems[1]);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int idx = info.position;
//
//        final Cursor activitiesHistoryCursor = MainActivity.activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
//                COL1 + ", " +
//                COL2 + ", " +
//                COL3 + ", " +
//                COL4 + ", " +
//                COL5 + ", " +
//                COL6 + " FROM " +
//                TABLE_NAME + " ORDER BY " +
//                COL2, null);
//
//        activitiesHistoryCursor.moveToFirst();
//        activitiesHistoryCursor.move(idx);
//
//        int menuItemIdx = item.getItemId();
//        switch (menuItemIdx) {
//            case 0:
//                final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.history_edit_dialog, null);
////                Toast.makeText(getContext(), dialogView.toString(), Toast.LENGTH_LONG).show();
//                EditText temp = dialogView.findViewById(R.id.historyEditDialogNameField);
//                Toast.makeText(getContext(), temp.toString(), Toast.LENGTH_LONG).show();
//                temp.setText("Enter new activity name");
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Edit activity")
//                        .setView(R.layout.history_edit_dialog)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ContentValues values = new ContentValues(2);
////                                Toast.makeText(getContext(), temp.getText().toString(), Toast.LENGTH_SHORT).show();
//                                values.put(COL1, ((EditText) dialogView.findViewById(R.id.historyEditDialogNameField)).getText().toString());
//                                values.put(COL2, activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL2)));
//                                values.put(COL3, activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL3)));
//                                values.put(COL4, activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL4)));
//                                values.put(COL5, activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL5)));
//                                values.put(COL6, activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL6)));
//
//                                MainActivity.activityLog.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " LIKE \'" +
//                                        activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL2)) + "\'");
//                                MainActivity.activityLog.insert(TABLE_NAME, COL1, values);
//
//                                Cursor constantsCursor = MainActivity.activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
//                                        COL1 + ", " + COL2 + ", " +
//                                        COL3 + ", " + COL4 + ", " +
//                                        COL5 + ", " + COL6 + " FROM " +
//                                        TABLE_NAME + " ORDER BY " +
//                                        COL2, null);
//                                ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_activity_history, constantsCursor, new String[]
//                                        {COL1, COL2, COL3, COL4},
//                                        new int[]{R.id.list_activity_name, R.id.list_startTime, R.id.list_latitude, R.id.list_longtitude}, 0);
//                                setListAdapter(adapter);
//                                registerForContextMenu(getListView());
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();
//
//
//                break;
//            case 1:
//                MainActivity.activityLog.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " = " + activitiesHistoryCursor.getString(activitiesHistoryCursor.getColumnIndex(COL2)));
//                Cursor constantsCursor = MainActivity.activityLog.rawQuery("SELECT " + BaseColumns._ID + ", " +
//                        COL1 + ", " +
//                        COL2 + ", " +
//                        COL3 + ", " +
//                        COL4 + ", " +
//                        COL5 + ", " +
//                        COL6 + " FROM " +
//                        TABLE_NAME + " ORDER BY " +
//                        COL2, null);
//                ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_activity_history, constantsCursor, new String[]
//                        {COL1, COL2, COL3, COL4},
//                        new int[]{R.id.list_activity_name, R.id.list_startTime, R.id.list_latitude, R.id.list_longtitude}, 0);
//                setListAdapter(adapter);
//                registerForContextMenu(getListView());
//
//
//                break;
//        }
//        return true;
//    }
}
