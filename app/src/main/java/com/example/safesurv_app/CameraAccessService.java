package com.example.safesurv_app;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraAccessService extends Service {
    private UsageStatsManager usageStatsManager;
    private Apps_Database database;

    private CameraManager cameraManager;
    private boolean isCameraOn = false;

    private static final int REQUEST_CODE_USAGE_STATS = 1001;

    // Check if the permission is granted
    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    // Request permission
    private void requestUsageStatsPermission() {
        if (!hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private String getForegroundApp() {
        if (hasUsageStatsPermission()) {
            usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);

            if (usageStatsList != null && !usageStatsList.isEmpty()) {
                // Find the app with the most recent usage
                UsageStats recentStats = null;
                for (UsageStats usageStats : usageStatsList) {
                    if (recentStats == null || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                if (recentStats != null) {
                    return recentStats.getPackageName();
                }
            }
        }
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        requestUsageStatsPermission();// Create the notification channel
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification(); // Create the notification
        startForeground(1, notification);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // You can set up a Handler to periodically check the camera state
        Handler handler = new Handler();
        handler.postDelayed(this::checkCameraState, 1000); // Check every second

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "camera_channel";
            String channelName = "Camera Monitoring Service";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        String channelId = "camera_channel"; // Use the same ID as your notification channel
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Camera Monitoring Service")
                .setContentText("Monitoring camera usage...")
                .setSmallIcon(R.drawable.camera) // Ensure you have a valid icon
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set priority for the notification
                .setOngoing(true); // Makes the notification ongoing

        return builder.build();
    }

    private void checkCameraState() {
        // Check the camera state
        /*try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                boolean cameraAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                if (cameraAvailable && !isCameraOn) {
                    isCameraOn = true;
                    String appName = getForegroundApp();
                    if(appName!="com.example.safesurv_app")
                        logCameraStatus("Camera is ON, used by: " + appName);
                    //logCameraStatus("Camera is ON");
                } else if (!cameraAvailable && isCameraOn) {
                    isCameraOn = false;
                   *//*logCameraStatus("Camera is OFF");*//*
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Repeat the check
        new Handler().postDelayed(this::checkCameraState, 1000);*/ // Check every second
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                boolean cameraAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                if (cameraAvailable && !isCameraOn) {
                    isCameraOn = true;
                    String appName = getForegroundApp();

                    // Broadcast the camera usage
                    Intent intent = new Intent("com.example.safesurv_app.CAMERA_USAGE");
                    intent.putExtra("appName", appName);
                    sendBroadcast(intent);

                    logCameraStatus("Camera is ON, used by: " + appName);
                } else if (!cameraAvailable && isCameraOn) {
                    isCameraOn = false;

                    // Broadcast the camera being turned off
                    Intent intent = new Intent("com.example.safesurv_app.CAMERA_USAGE");
                    intent.putExtra("appName", ""); // Empty appName to signify camera off
                    sendBroadcast(intent);

                    logCameraStatus("Camera is OFF");
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Repeat the check
        new Handler().postDelayed(this::checkCameraState, 1000); // Check every second
    }

    private void logCameraStatus(String status) {
        String currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        }
        Log.d("CameraMonitoringService", status + " at " + currentTime);
        // You can also write this to a file or a database if needed
    }
}
