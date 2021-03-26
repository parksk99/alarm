package com.example.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            String channelID = intent.getStringExtra("contentTitle") + intent.getStringExtra("contentText");
            int requestCode = intent.getIntExtra("time", 0) + intent.getStringExtra("contentText").hashCode();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.channel_name);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String description = context.getString(R.string.channel_description);
                NotificationChannel channel = new NotificationChannel(channelID, name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_tmp_noti_icon)
                .setContentTitle(intent.getExtras().getString("contentTitle"))
                .setContentText(intent.getExtras().getString("contentText"))
                .setDefaults(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(requestCode, builder.build());
            //notificationManager.cancel(100);      //notify할때 지정한 id(100)에 해당하는 알림 삭제

    }

}