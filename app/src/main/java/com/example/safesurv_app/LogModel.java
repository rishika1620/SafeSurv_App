package com.example.safesurv_app;

public class LogModel {
        private String startTime;
        private String packageName;
        private String endTime;

        public LogModel(String startTime, String packageName, String endTime) {
            this.startTime = startTime;
            this.packageName = packageName;
            this.endTime = endTime;
        }

        // Getters and Setters
        public String getStartTime() {
            return startTime;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getEndTime() {
            return endTime;
        }

}
