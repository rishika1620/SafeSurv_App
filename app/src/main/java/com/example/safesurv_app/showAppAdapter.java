package com.example.safesurv_app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class showAppAdapter extends RecyclerView.Adapter<showAppAdapter.CameraAppViewHolder> {

    private Apps_Database dbHelper;
    private List<ApplicationInfo> cameraAppList;
    private Context context;
    private PackageManager packageManager;

    public showAppAdapter(Context context, List<ApplicationInfo> cameraAppList) {
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
        // ApplicationInfo packageInfo = cameraAppList.get(position);
        ApplicationInfo appInfo = cameraAppList.get(position);

        // Set app name
        holder.appNameTextView.setText(packageManager.getApplicationLabel(appInfo));
        // Set app icon
        Drawable appIcon = appInfo.loadIcon(packageManager);
        holder.appIconImageView.setImageDrawable(appIcon);

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
}


