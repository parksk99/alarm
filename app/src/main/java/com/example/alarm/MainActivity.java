package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final String CHANNEL_ID = "ChannerID";
    private NotificationManager notificationManager;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("박성규 맞춤 알람");
        Button alarm = findViewById(R.id.alarm);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);   //12시간 표기방법에서 24시간 표기방법으로 바꿈
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        createNotificationChannel();
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = getTimePicker();    //timePicker로부터 시간을 가져오고 그 시간을 alarmManager에게 전달
                //alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), ); //마지막 인자 : pendingIntent
                sendNotification();
            }
        });

    }

    //timePicker에 저장된 시간을 가져옴
    private Calendar getTimePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour, min;

        //timePicker가 23버전부터 getHour를 사용하게 함
        //이전 버전과 호환을 위해 if로 구
        if(Build.VERSION.SDK_INT >= 23){
            hour = timePicker.getHour();
            min = timePicker.getMinute();
        }
        else{
            hour = timePicker.getCurrentHour();
            min = timePicker.getCurrentMinute();
        }
       // calendar.set()
    return calendar;    //****calendar에 hour와 min을 저장해야
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("test")
                .setContentText("alarm test")
                .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);
        return builder;
    }
    private void sendNotification(){
        Toast toast = Toast.makeText(this, "hi", Toast.LENGTH_SHORT);
        toast.show();
        notificationManager.notify(100, getNotificationBuilder().build());
    }
    private void createNotificationChannel(){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = getString(R.string.channel_name);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                String description = getString(R.string.channel_description);
                NotificationChannel channel = new NotificationChannel("alarm", name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }
    }
}