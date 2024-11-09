package com.example.safesurv_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Apps_Database extends SQLiteOpenHelper {
    // Database Name
    private static final String DATABASE_NAME = "CameraLogs.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_NAME = "CameraLogsStored";

    // Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_APP_NAME = "appname";
    public static final String COLUMN_PACKAGE_NAME = "packagename";
    public static final String COLUMN_START_TIME = "starttime";
    public static final String COLUMN_END_TIME = "endtime";
    public static final String COLUMN_DURATION = "duration";

    public Apps_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*public static synchronized Apps_Database getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_APP_NAME + " TEXT, "
                + COLUMN_PACKAGE_NAME + " TEXT, "
                + COLUMN_START_TIME + " TEXT, "
                + COLUMN_END_TIME + " TEXT, "
                + COLUMN_DURATION + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create table again
        onCreate(db);
    }

    // Insert a new camera access log
    public long insertLog(String appName, String packageName, String startTime) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APP_NAME, appName);
        values.put(COLUMN_PACKAGE_NAME, packageName);
        values.put(COLUMN_START_TIME, startTime);

        // Insert a new log entry and return the row ID of the newly inserted row
        long rowId = database.insert(TABLE_NAME, null, values);
        database.close();
        Log.d("Database","Inserted in Database");
        // Close the database connection
        return rowId;
    }

    // Update the camera log with end time and calculate duration
    public int updateLogWithEndTime(String packageName, String endTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d("Database","Updating Log");
        // Query to get the start time based on the package name
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COLUMN_START_TIME},
                COLUMN_PACKAGE_NAME + " = ? AND " + COLUMN_END_TIME + " IS NULL",
                new String[]{packageName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));

            // Calculate the duration between startTime and endTime
            String duration = calculateDuration(startTime, endTime);

            // Update the log with end time and duration
            ContentValues values = new ContentValues();
            values.put(COLUMN_END_TIME, endTime);
            values.put(COLUMN_DURATION, duration);

            int rowsAffected = database.update(TABLE_NAME, values,
                    COLUMN_PACKAGE_NAME + " = ? AND " + COLUMN_END_TIME + " IS NULL",
                    new String[]{packageName});

            cursor.close(); // Close the cursor
            database.close(); // Close the database connection
            return rowsAffected;
        } else if (cursor != null) {
            cursor.close(); // Close the cursor
        }
        database.close(); // Close the database connection
        return 0; // No entry found to update
    }

    // Calculate the duration between start time and end time
    private String calculateDuration(String startTime, String endTime) {
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
        try {
            Date start = null;
            Date end = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                start = sdf.parse(startTime);
                end = sdf.parse(endTime);
            }
            long durationInMillis = end.getTime() - start.getTime();
            long seconds = (durationInMillis / 1000) % 60;
            long minutes = (durationInMillis / (1000 * 60)) % 60;
            long hours = (durationInMillis / (1000 * 60 * 60)) % 24;
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "00:00:00"; // Return default duration if parsing fails
    }

    public Cursor fetchCameraLogsByAppName(String appName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Use a parameterized query to prevent SQL injection
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_APP_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{appName});

        db.close(); // Close the database
        return cursor;
    }

}
