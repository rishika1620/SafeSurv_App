package com.example.safesurv_app;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

    public class CameraLogAdapter extends RecyclerView.Adapter<CameraLogAdapter.ViewHolder> {
        private List<LogModel> cameraLogs;

        public CameraLogAdapter(List<LogModel> cameraLogs) {
            this.cameraLogs = cameraLogs;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_log_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LogModel log = cameraLogs.get(position);
            holder.startTimeTextView.setText("Start Time: " + log.getStartTime());
            holder.packageNameTextView.setText("Package Name: " + log.getPackageName());
            holder.endTimeTextView.setText("End Time: " + log.getEndTime());
        }

        @Override
        public int getItemCount() {
            return cameraLogs.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView startTimeTextView;
            TextView packageNameTextView;
            TextView endTimeTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                startTimeTextView = itemView.findViewById(R.id.start_time);
                packageNameTextView = itemView.findViewById(R.id.package_name);
                endTimeTextView = itemView.findViewById(R.id.end_time);
            }
        }
    }

