package com.example.safesurv_app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CameraAppAdapter extends RecyclerView.Adapter<CameraAppAdapter.CameraAppViewHolder> {

    private List<PackageInfo> cameraAppList;
    private Context context;
    private PackageManager packageManager;

    public CameraAppAdapter(Context context, List<PackageInfo> cameraAppList) {
        this.context = context;
        this.cameraAppList = cameraAppList;
        this.packageManager = context.getPackageManager();
    }

    @NonNull
    @Override
    public CameraAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_app_item, parent, false);
        return new CameraAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraAppViewHolder holder, int position) {
        PackageInfo packageInfo = cameraAppList.get(position);
        //ApplicationInfo appInfo = cameraAppList.get(position);

        // Set app name
        holder.appNameTextView.setText(packageInfo.packageName);

        // Set app icon
        //Drawable appIcon = appInfo.loadIcon(packageManager);
        //holder.appIconImageView.setImageDrawable(appIcon);
    }

    @Override
    public int getItemCount() {
        return cameraAppList.size();
    }

    public static class CameraAppViewHolder extends RecyclerView.ViewHolder {

        TextView appNameTextView;
        ImageView appIconImageView;

        public CameraAppViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
        }
    }
}

