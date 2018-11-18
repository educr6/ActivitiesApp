package example.com.stepsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns{

    public static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 1;


    public static final String TABLE_NAME = "activity_table";
    //public static final String COL0 = "ID";
    public static final String COL1 = "activity_name";
    public static final String COL2 = "start_time";
    public static final String COL3 = "position_latitude";
    public static final String COL4 = "position_longtitude";

    private static final String CREATE_ENTERIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, " + COL1 + " TEXT, " + COL2 +
            " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT)";
    private static final String DELETE_ENTERIES = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ENTERIES);
        ContentValues cv = new ContentValues();

        cv.put(COL1, "Init");
        cv.put(COL2, MainActivity.printDateFormat.format(Calendar.getInstance().getTime()));
        cv.put(COL3, "-1");
        cv.put(COL4, "-1");
        db.insert(TABLE_NAME, COL1, cv);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ENTERIES);
        onCreate(db);
    }
}
