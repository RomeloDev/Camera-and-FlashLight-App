package com.example.cameraandflashlightapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView imageview;
    Button cameraBtn, flashlightBtn;
    boolean isFlashlightOn = false;
    CameraManager cameraManager;
    String cameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageview = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.camera);

         if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
         != PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},100);
         }

         cameraBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(intent, 100);
             }
         });

         flashlightBtn = findViewById(R.id.flashlight);
         cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

         try {
             cameraID = cameraManager.getCameraIdList()[0];
         }catch (CameraAccessException e){
             e.printStackTrace();
         }

         flashlightBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(isFlashlightOn){
                     turnOffFlashlight();
                 }else {
                     turnOnFlashlight();
                 }
             }
         });
    }

    private void turnOnFlashlight() {
        try {
            cameraManager.setTorchMode(cameraID, true);
            isFlashlightOn = true;
            flashlightBtn.setText("OFF");
            flashlightBtn.setBackgroundColor(Color.parseColor("#32CD32"));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlashlight() {
        try {
            cameraManager.setTorchMode(cameraID, false);
            isFlashlightOn = false;
            flashlightBtn.setText("ON");
            flashlightBtn.setBackgroundColor(Color.parseColor("#FF0000"));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(bitmap);
        }
    }
}