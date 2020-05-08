package com.yunjeapark.technote.TN_Utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.androidquery.AQuery;
import com.yunjeapark.technote.MainActivity;
import com.yunjeapark.technote.R;

public class UtilityTech extends AppCompatActivity implements View.OnClickListener {
    AQuery aQuery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_tech);

        initView();
    }

    public void initView(){
        aQuery = new AQuery(this);
        aQuery.id(R.id.bt_util_notification).visible().enabled(true).clicked(this);
        aQuery.id(R.id.bt_util_dpi_check).visible().enabled(true).clicked(this);
    }

    @Override
    public void onClick(View v) {
        if(v == aQuery.id(R.id.bt_util_notification).getView()){
            myNotification();
        }else if(v == aQuery.id(R.id.bt_util_dpi_check).getView()){
            displayCheck();
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26이상 작동
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void myNotification(){
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(),"0")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSmallIcon(R.drawable.tech_note_icon)
                        .setContentTitle("TechNote")
                        .setContentText("Image upload")
                        .setContentIntent(pendingIntent) // 상단바 아이콘을 누르면 해당 Activity로 이동
                        .setAutoCancel(true); // notification을 누르면 자동으로제거

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(0,mBuilder.build());
    }

    private void displayCheck(){
        String dpi = null;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (metrics.densityDpi<=160) { // mdpi
            dpi = "mdpi";
        } else if (metrics.densityDpi<=240) { // hdpi
            dpi = "hdpi";
        } else if (metrics.densityDpi<=320) { // xhdpi
            dpi = "xhdpi";
        } else if (metrics.densityDpi<=480) { // xxhdpi
            dpi = "xxhdpi";
        } else if (metrics.densityDpi<=640) { // xxxhdpi
            dpi = "xxxhdpi";
        }

        Toast.makeText(this, "dpi => " + metrics.densityDpi + "(" + dpi + ")\n" +
                "display => size.x : " + size.x + ", size.y : " + size.y, Toast.LENGTH_SHORT).show();

        Log.d("deviceInformation", "dpi => " + metrics.densityDpi + "(" + dpi + ")");
        Log.d("deviceInformation", "display => size.x : " + size.x + ", size.y : " + size.y);
    }
}
