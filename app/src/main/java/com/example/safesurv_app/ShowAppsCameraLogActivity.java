package com.example.safesurv_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShowAppsCameraLogActivity extends AppCompatActivity {
    Apps_Database database;
    Cursor cursor;
    RecyclerView recyclerViewCameraLogs;
    CameraLogAdapter cameraLogAdapter;
    List<LogModel> cameraLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_apps_camera_log);
        database = new Apps_Database(this);

        Intent intent = getIntent();

        // Retrieve the data using the key
        String value = intent.getStringExtra("app_name");

        cursor = database.fetchCameraLogsByAppName(value);
        cameraLogs = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // Fetch the details from the cursor
                    int startTimeIndex = cursor.getColumnIndex(database.COLUMN_START_TIME);
                    int packageNameIndex = cursor.getColumnIndex(database.COLUMN_PACKAGE_NAME);
                    int endTimeIndex = cursor.getColumnIndex(database.COLUMN_END_TIME);

                    // Check if the indices are valid (≥ 0)
                    if (startTimeIndex >= 0 && packageNameIndex >= 0 && endTimeIndex >= 0) {
                        // Fetch the details from the cursor
                        String startTime = cursor.getString(startTimeIndex);
                        String packageName = cursor.getString(packageNameIndex);
                        String endTime = cursor.getString(endTimeIndex);

                        Log.d("Showing Logs", "Start time = " + startTime + "End time = " + endTime);

                        // Create a new CameraLog object and add it to the list
                        LogModel log = new LogModel(startTime, packageName, endTime);
                        cameraLogs.add(log);
                    }else{
                        Log.w("DatabaseWarning", "Column index is invalid for the specified column names.");
                    }
                } while (cursor.moveToNext());
            }
            cursor.close(); // Always close the cursor
        }

        recyclerViewCameraLogs = findViewById(R.id.recyclerViewAppsLogs);
        recyclerViewCameraLogs.setLayoutManager(new LinearLayoutManager(this));
        cameraLogAdapter = new CameraLogAdapter(cameraLogs);
        recyclerViewCameraLogs.setAdapter(cameraLogAdapter);


    }

}
