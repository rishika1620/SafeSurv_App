package com.example.safesurv_app;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppAccessingCameraActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private showAppAdapter cameraAppAdapter;
    private Apps_Database dbHelper;
    SearchView searchViewCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_accessing_camera);
        searchViewCamera = findViewById(R.id.searchView_cameraapps);
        //private CameraLogAdapter adapter;

        getSupportActionBar().setTitle("App Accessing Camera");

        /*dbHelper = new Apps_Database(this);
        fetchAndStoreAppsInfo();
        showtoRecyclerView();*/


        recyclerView = findViewById(R.id.recyclerViewCamera);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of camera apps
        List<ApplicationInfo> cameraApps = getCameraApps();
        // Set the adapter for RecyclerView
        cameraAppAdapter = new showAppAdapter(this, cameraApps);
        recyclerView.setAdapter(cameraAppAdapter);

        getSupportActionBar().setSubtitle("Total : " + cameraApps.size());

        searchViewCamera.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cameraAppAdapter.filter(newText);
                return false;
            }
        });

    }

    // Method to fetch all installed apps with CAMERA permission
    private List<ApplicationInfo> getCameraApps() {
        List<ApplicationInfo> cameraApps = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // Get a list of all installed apps
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Iterate through the installed apps
        for (ApplicationInfo appInfo : installedApps) {
            try {
                // Check if the app has declared CAMERA permission
                if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(Manifest.permission.CAMERA, appInfo.packageName)) {
                    cameraApps.add(appInfo); // Add to the list if it has camera permission
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cameraApps; // Return the list of apps with camera permission*/
    }


}

