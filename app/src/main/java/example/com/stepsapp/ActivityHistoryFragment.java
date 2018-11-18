package example.com.stepsapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import example.com.stepsapp.dummy.DummyContent;
import example.com.stepsapp.dummy.DummyContent.DummyItem;

import java.util.List;



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
                DatabaseHelper.COL1 + ", " +
                DatabaseHelper.COL2 + ", " +
                DatabaseHelper.COL3 + ", " +
                DatabaseHelper.COL4 + " FROM " +
                DatabaseHelper.TABLE_NAME + " ORDER BY " +
                DatabaseHelper.COL2, null);
        ListAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.fragment_activity_history, constantsCursor, new String[]
                {DatabaseHelper.COL1, DatabaseHelper.COL2, DatabaseHelper.COL3, DatabaseHelper.COL4},
                new int[] {R.id.list_activity_name, R.id.list_startTime, R.id.list_latitude, R.id.list_longtitude}, 0);
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
}
