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
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final String CHANNEL_ID = "ChannerID";
    private NotificationManager notificationManager;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private EditText editText;
    private Button delButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        delButton = findViewById(R.id.delete);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예약된 알림을 개대충 삭제함
                //PedingIntent.getBroadcast의 두번째 인자가 중요함 마지막도 중요함 (알림 생성할때랑 똑같이 해야됨)
                alarmManager.cancel(PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
            }
        });
        setTitle("박성규 맞춤 알람");
        Button alarm = findViewById(R.id.alarm);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);   //12시간 표기방법에서 24시간 표기방법으로 바꿈
        editText = findViewById(R.id.editText);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        class AlarmRunnable implements Runnable {

            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                receiverIntent.putExtra("contentTitle", "contentTitle");
                receiverIntent.putExtra("contentText", "aa");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);
//                try {
//                    calendar.setTime(dateFormat.parse("2021-03-16 11:25:00"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        //createNotificationChannel();
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = getTime();    //timePicker로부터 시간을 가져오고 그 시간을 alarmManager에게 전달
//                AlarmRunnable ar = new AlarmRunnable();
//                Thread t = new Thread(ar);
//                t.start();
                setAlarm(calendar);
            }
        });

    }

    private void setAlarm(Calendar calendar) {
        Toast toast = Toast.makeText(this, "설정 시간 : " + calendar.getTime() + editText.getText().toString(), Toast.LENGTH_SHORT);
        toast.show();
        String content = editText.getText().toString();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        receiverIntent.putExtra("contentTitle", calendar.get(Calendar.HOUR)+"시 "+calendar.get(Calendar.MINUTE)+"분");
        receiverIntent.putExtra("contentText", content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT); //마지막 인자 : receiverIntent의 Extras 값을 최신으로 유지하게 함


//        try {
//            calendar.setTime(dateFormat.parse("2021-03-16 11:25:00"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60/*AlarmManager.INTERVAL_DAY*7*/, pendingIntent); //알림 반복 설정 : 세번째 인자가 반복 주기
    }

    //timePicker에 저장된 시간을 가져옴
    private Calendar getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int hour, min;
//        timePicker가 23버전부터 getHour를 사용하게 함
//        이전 버전과 호환을 위해 if로 구현
        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.getHour();
            min = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            min = timePicker.getCurrentMinute();
        }
//        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        return calendar;    //****calendar에 요일을 저장해야
    }

    //    private NotificationCompat.Builder getNotificationBuilder(String contentTitle, String contentText){
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle(contentTitle)
//                .setContentText(contentText)
//                .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(notificationPendingIntent)
//                .setAutoCancel(true);
//        return builder;
//    }
//    private void sendNotification(){
//        Toast toast = Toast.makeText(this, "hi", Toast.LENGTH_SHORT);
//        toast.show();
//        notificationManager.notify(100, getNotificationBuilder("title", "text").build());
//    }
    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }
}