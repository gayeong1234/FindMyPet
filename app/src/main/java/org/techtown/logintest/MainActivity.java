package org.techtown.logintest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_pass;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        double distance;

        Location locationA = new Location("pointA");
        locationA.setLatitude(36.35164207622783);
        locationA.setLongitude(127.38749763950986);

        Location locationB = new Location("pointB");
        locationB.setLatitude(36.355433);
        locationB.setLongitude(127.381850);

        distance = locationA.distanceTo(locationB);

        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(distance <100){
                    Intent intent = new Intent(
                            getApplicationContext(),
                            Level1Activity.class);
                    startActivity(intent);
                } else if(distance>=100&&distance<500){
                    Intent intent = new Intent(
                            getApplicationContext(),
                            Level2Activity.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(
                            getApplicationContext(),
                            Level3Activity.class);
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");

        tv_id.setText(userID);
        tv_pass.setText(userPass);

    }
}