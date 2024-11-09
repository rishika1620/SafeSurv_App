// CameraAccessReceiver.java
package com.example.safesurv_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class CameraAccessReceiver extends BroadcastReceiver {

    private CameraAccessListener listener;

    public CameraAccessReceiver(CameraAccessListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getStringExtra("package_name");

        if (intent.getAction().equals("com.example.ACTION_CAMERA_USED")) {
            boolean isUsingCamera = intent.getBooleanExtra("is_using_camera", false);
            listener.onCameraUsageDetected(packageName, isUsingCamera);
        }
    }

    public interface CameraAccessListener {
        void onCameraUsageDetected(String packageName, boolean isUsingCamera);
    }
}

