package com.example.safesurv_app;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppsAccessingInternetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private showAppAdapter internetAppAdapter;
    Cursor cursor;

    SwitchCompat switchPermissionsCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_apps_accessing_internet);

        //switchPermissionsCamera = findViewById(R.id.swicthPermission);

        getSupportActionBar().setTitle("App Accessing Internet");

        recyclerView = findViewById(R.id.recyclerViewInternet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of camera apps
        List<ApplicationInfo> internetApps = getInternetApps();
        // Set the adapter for RecyclerView
        internetAppAdapter = new showAppAdapter(this, internetApps);
        recyclerView.setAdapter(internetAppAdapter);

        getSupportActionBar().setSubtitle("Total : " + internetApps.size());

    }

    // Method to fetch all installed apps with CAMERA permission
    private List<ApplicationInfo> getInternetApps() {
        List<ApplicationInfo> internetApps = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // Get a list of all installed apps
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Iterate through the installed apps
        for (ApplicationInfo appInfo : installedApps) {
            try {
                // Check if the app has declared CAMERA permission
                if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(Manifest.permission.INTERNET, appInfo.packageName)) {
                    // switchPermissionsCamera.setChecked(true);
                    internetApps.add(appInfo); // Add to the list if it has camera permission
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return internetApps; // Return the list of apps with camera permission*/
    }

}