package com.example.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        }

        //알림창 클릭 시 activity 화면 부름
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentTitle("알람");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(1,notification);

    }
}



//        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder = new NotificationCompat.Builder(context,"")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle(intent.getStringExtra("contentTitle"))
//                .setContentText(intent.getStringExtra("contentText"))
//                .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(notificationPendingIntent)
//                .setAutoCancel(true);
//        notificationManager.notify(100, builder.build());





