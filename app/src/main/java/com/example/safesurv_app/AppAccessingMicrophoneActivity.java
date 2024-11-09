package com.example.safesurv_app;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppAccessingMicrophoneActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private showAppAdapter microphoneAppAdapter;
    SearchView searchViewMicrophone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_accessing_microphone);
        searchViewMicrophone = findViewById(R.id.searchView_microphoneapps);

        getSupportActionBar().setTitle("App Accessing Microphone");

        recyclerView = findViewById(R.id.recyclerViewMicrophone);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of camera apps
        List<ApplicationInfo> microphoneApps = getMicrophoneApps();
        // Set the adapter for RecyclerView
        microphoneAppAdapter = new showAppAdapter(this, microphoneApps);
        recyclerView.setAdapter(microphoneAppAdapter);
        getSupportActionBar().setSubtitle("Total : " + microphoneApps.size());

        searchViewMicrophone.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                microphoneAppAdapter.filter(newText);
                return false;
            }
        });
    }

    private List<ApplicationInfo> getMicrophoneApps() {
        List<ApplicationInfo> microphoneApps = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // Get a list of all installed apps
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Iterate through the installed apps
        for (ApplicationInfo appInfo : installedApps) {
            try {
                // Check if the app has declared CAMERA permission
                if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(Manifest.permission.RECORD_AUDIO, appInfo.packageName)) {
                   // switchPermissionsMicrophone.setChecked(true);
                    microphoneApps.add(appInfo); // Add to the list if it has camera permission
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return microphoneApps; // Return the list of apps with camera permission*/
    }
}