package org.techtown.logintest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapPolygon;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    private static String CHANNEL_ID2 = "channel2";
    private static String CHANNEL_NAME2 = "Channel2";

    String API_Key = "l7xxf42d251eaeaa49efab37edd020c62857";

    // T Map View
    TMapView tMapView = null;

    NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        double distance;
        //String meter;

        Location locationA = new Location("pointA");
        locationA.setLatitude(36.35164207622783);
        locationA.setLongitude(127.38749763950986);

        Location locationB = new Location("pointB");
        locationB.setLatitude(36.355433);
        locationB.setLongitude(127.381850);

        distance = locationA.distanceTo(locationB);
        //meter = Double.toString(distance);

        if(distance>=500){
            showNoti2();
        } else if(distance>=100){
            showNoti1();
        }

        Button buttonZoomIn = (Button)findViewById(R.id.buttonZoomIn);
        Button buttonZoomOut = (Button)findViewById(R.id.buttonZoomOut);

        // "확대" 버튼 클릭
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });

        // "축소" 버튼 클릭
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });

        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        // Initial Setting
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);

        final ArrayList alTMapPoint = new ArrayList();
        alTMapPoint.add(new TMapPoint(36.35164207622783, 127.38749763950986));//kfc 시청
        alTMapPoint.add(new TMapPoint(36.355433, 127.381850));//올리브영 시청

        // 마커 아이콘
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.poi_dot);

        for (int i = 0; i < alTMapPoint.size(); i++) {
            TMapMarkerItem tmapMarkerItem = new TMapMarkerItem();
            // 마커 아이콘 지정
            tmapMarkerItem.setIcon(bitmap);
            // 마커의 좌표 지정
            tmapMarkerItem.setTMapPoint((TMapPoint) alTMapPoint.get(i));
            //지도에 마커 추가
            tMapView.addMarkerItem("markerItem" + i, tmapMarkerItem);

            if(i == 0) {
                tmapMarkerItem.setName("사용자 현재 위치");
                String MarkerName1 = tmapMarkerItem.getName();
                tmapMarkerItem.setCalloutTitle("사용자 현재 위치");
                String msg1 = tmapMarkerItem.getCalloutTitle();
                tmapMarkerItem.setAutoCalloutVisible(true);
            } else{
                tmapMarkerItem.setName("아두이노 현재 위치");
                String MarkerName2 = tmapMarkerItem.getName();
                tmapMarkerItem.setCalloutTitle("아두이노 현재 위치");
                String msg2 = tmapMarkerItem.getCalloutTitle();
                tmapMarkerItem.setAutoCalloutVisible(true);
            }


            tMapView.setCenterPoint(127.387497, 36.351642);
        }

        ArrayList<TMapPoint> altTMapPoint = new ArrayList<TMapPoint>();
        altTMapPoint.add( new TMapPoint(36.35164207622783, 127.38749763950986) ); // kfc 시청
        altTMapPoint.add( new TMapPoint(36.355433, 127.381850) ); // 올리브영

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.BLUE);
        tMapPolyLine.setLineWidth(2);
        for( int i=0; i<alTMapPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( altTMapPoint.get(i) );
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNoti1(){

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) != null) {
                manager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                ));

                builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            }
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(this, Level2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("Find My Pet");
        builder.setContentText("반려동물과 100m 이상 멀어졌습니다.");
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        );

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(3000);

        MediaPlayer player = MediaPlayer.create(this, R.raw.ring);
        player.start();

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNoti2() {

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) != null) {
                manager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID2, CHANNEL_NAME2, NotificationManager.IMPORTANCE_HIGH
                ));

                builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            }
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(this, Level3Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("Find My Pet");
        builder.setContentText("반려동물과 500m 이상 멀어졌습니다.");
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID2, CHANNEL_NAME2, NotificationManager.IMPORTANCE_HIGH
        );

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(3000);

        MediaPlayer player = MediaPlayer.create(this, R.raw.ring);
        player.start();

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }
}