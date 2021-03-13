package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String CHANNEL_ID = "ChannerID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("박성규 맞춤 알람");
        Button alarm = findViewById(R.id.alarm);

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("test")
                        .setContentText("alarm test")
                        .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, builder.build());
             //   createNotificationChannel();
            }
        });

    }

    private void createNotificationChannel(){


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Toast toast = Toast.makeText(this, "hi", Toast.LENGTH_SHORT);
                toast.show();
                CharSequence name = getString(R.string.channel_name);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                String description = getString(R.string.channel_description);
                NotificationChannel channel = new NotificationChannel("alarm", name, importance);
                channel.setDescription(description);


            }

    }
}