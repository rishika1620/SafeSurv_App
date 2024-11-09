package com.example.safesurv_app;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.AppInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class showAppAdapter extends RecyclerView.Adapter<showAppAdapter.CameraAppViewHolder> {

    private Apps_Database dbHelper;
    private List<ApplicationInfo> cameraAppList;
    private Context context;
    private PackageManager packageManager;
    private List<ApplicationInfo> appListFull;

    public showAppAdapter(Context context, List<ApplicationInfo> cameraAppList) {
        this.context = context;
        this.cameraAppList = cameraAppList;
        this.packageManager = context.getPackageManager();
        sortCameraAppsByName(cameraAppList, packageManager);
        this.appListFull = new ArrayList<>(cameraAppList);
    }

    @NonNull
    @Override
    public CameraAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_app_item, parent, false);
        return new CameraAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraAppViewHolder holder, int position) {
        // ApplicationInfo packageInfo = cameraAppList.get(position);
        ApplicationInfo appInfo = cameraAppList.get(position);

        // Set app name
        holder.appNameTextView.setText(packageManager.getApplicationLabel(appInfo));
        // Set app icon
        Drawable appIcon = appInfo.loadIcon(packageManager);
        holder.appIconImageView.setImageDrawable(appIcon);

        if(isAppInForeground(context,appInfo.packageName))
            holder.permission_state.setChecked(true);
        else
            holder.permission_state.setChecked(false);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowAppsCameraLogActivity.class);
                intent.putExtra("app_name",packageManager.getApplicationLabel(appInfo));
                context.startActivity(intent);
            }
                                           }
        );
    }


    @Override
    public int getItemCount() {
        return cameraAppList.size();
    }

    public static class CameraAppViewHolder extends RecyclerView.ViewHolder {

        TextView appNameTextView;
        ImageView appIconImageView;
        SwitchCompat permission_state;

        public CameraAppViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
            permission_state = itemView.findViewById(R.id.swicthPermission);
        }
    }

    public void sortCameraAppsByName(List<ApplicationInfo> cameraApps, PackageManager packageManager) {
        Collections.sort(cameraApps, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo app1, ApplicationInfo app2) {
                String appName1 = app1.loadLabel(packageManager).toString();
                String appName2 = app2.loadLabel(packageManager).toString();
                return appName1.compareToIgnoreCase(appName2); // For case-insensitive sorting
            }
        });
    }

    public static boolean isAppInForeground(Context context, String targetPackageName) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        if (usageStatsManager == null) {
            Log.e("UsageStats", "UsageStatsManager is null");
            return false;
        }

        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1); // Check for the last 5 minutes of activity

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                calendar.getTimeInMillis(),
                currentTime
        );

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            SortedMap<Long, UsageStats> sortedUsageStatsMap = new TreeMap<>();
            for (UsageStats usageStats : usageStatsList) {
                sortedUsageStatsMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            if (!sortedUsageStatsMap.isEmpty()) {
                UsageStats recentUsageStats = sortedUsageStatsMap.get(sortedUsageStatsMap.lastKey());
                String currentForegroundPackage = recentUsageStats.getPackageName();

                // Check if the current foreground app matches the target package
                return targetPackageName.equals(currentForegroundPackage);
            }
        }

        return false; // App is not in the foreground
    }

    public void filter(String text) {
        cameraAppList.clear();
        if (text.isEmpty()) {
            cameraAppList.addAll(appListFull);
        } else {
            text = text.toLowerCase();
            for (ApplicationInfo app : appListFull) {
                if (app.loadLabel(packageManager).toString().toLowerCase().contains(text)) {
                    cameraAppList.add(app);
                }
            }
        }
        notifyDataSetChanged();
    }
}


