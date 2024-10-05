package com.example.safesurv_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    ImageView camera_icon;
    RelativeLayout homeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        camera_icon = findViewById(R.id.camera_access);
        //fragment_container = findViewById(R.id.frame_layout);
        homeLayout = findViewById(R.id.home_layout);

        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppAccessingCamera();
            }
        });
    }

    void openAppAccessingCamera(){
        Intent intent = new Intent(HomeActivity.this, AppAccessingCameraActivity.class);
        startActivity(intent);

    }
}