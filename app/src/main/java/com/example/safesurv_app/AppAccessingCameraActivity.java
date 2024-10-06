package com.example.safesurv_app;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppAccessingCameraActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CameraAppAdapter cameraAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_accessing_camera);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of camera apps
        List<PackageInfo> cameraApps = getCameraApps();
        // Set the adapter for RecyclerView
        cameraAppAdapter = new CameraAppAdapter(this, cameraApps);
        recyclerView.setAdapter(cameraAppAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void checkForegroundApp() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 1000 * 60 * 60;  // Last hour

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
            for (UsageStats usageStats : usageStatsList) {
                sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!sortedMap.isEmpty()) {
                String currentApp = sortedMap.get(sortedMap.lastKey()).getPackageName();
                Log.d("APP IN FOREGROUND", "Package: " + currentApp);

                // Check if the current app has camera permissions
                if (hasCameraPermission(currentApp)) {
                    // Handle unauthorized camera access
                    Log.d("Camera Access", "Unauthorized camera access by: " + currentApp);
                    // Optionally: Notify the user, block access, etc.
                }
            }
        }
    }

    // Check if the app has camera permissions
    private boolean hasCameraPermission(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions != null) {
                for (String permission : requestedPermissions) {
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to fetch all installed apps with CAMERA permission
    private List<PackageInfo> getCameraApps() {
        /*List<ApplicationInfo> cameraApps = new ArrayList<>();
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

        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        //StringBuilder cameraApps = new StringBuilder();
        List<PackageInfo> cameraApps = new ArrayList<>();


        for (PackageInfo packageInfo : installedPackages) {
            try {
                // Get the permissions requested by the app
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (String permission : requestedPermissions) {
                        // Check if the app has requested CAMERA permission
                        if (permission.equals(Manifest.permission.CAMERA)) {
                            // Add app to the list if it has CAMERA permission
                            //cameraApps.append(packageInfo.packageName).append("\n");
                            cameraApps.add(packageInfo);

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cameraApps;

    }

}

