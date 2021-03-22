package com.example.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.channel_name);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                String description = context.getString(R.string.channel_description);
                NotificationChannel channel = new NotificationChannel("", name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder = new NotificationCompat.Builder(context, "")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(intent.getExtras().getString("contentTitle"))
                    .setContentText(intent.getExtras().getString("contentText"))
                    .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(notificationPendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(100, builder.build());
            //notificationManager.cancel(100);      //notify할때 지정한 id(100)에 해당하는 알림 삭제

    }

}