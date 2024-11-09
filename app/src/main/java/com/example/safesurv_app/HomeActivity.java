package com.example.safesurv_app;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {
    ImageView camera_icon;
    ImageView microphone_icon;
    ImageView network_icon;
    RelativeLayout homeLayout;

    private static final int CAMERA_PERMISSION_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        camera_icon = findViewById(R.id.camera_access);
        microphone_icon = findViewById(R.id.microphone_access);
        network_icon = findViewById(R.id.network_access);
        //fragment_container = findViewById(R.id.frame_layout);
        homeLayout = findViewById(R.id.home_layout);

        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppAccessingCamera();
            }
        });

        microphone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppAccessingMicrophone();
            }
        });
        network_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppAccessingInternet();
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.PACKAGE_USAGE_STATS}, CAMERA_PERMISSION_CODE);
        }
        if (!hasUsageAccessPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


    }

    void openAppAccessingCamera(){
        Intent intent = new Intent(HomeActivity.this, AppAccessingCameraActivity.class);
        startActivity(intent);

    }

    void openAppAccessingMicrophone(){
        Intent intent = new Intent(HomeActivity.this, AppAccessingMicrophoneActivity.class);
        startActivity(intent);

    }

    void openAppAccessingInternet(){
        Intent intent = new Intent(HomeActivity.this, AppsAccessingInternetActivity.class);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Permission of Camera granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.PACKAGE_USAGE_STATS}, CAMERA_PERMISSION_CODE);
                Log.d("Permissions", "Permission of Camera not granted");
                // Permission denied
            }
        }
    }
    private boolean hasUsageAccessPermission() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName);
            return mode == AppOpsManager.MODE_ALLOWED;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}