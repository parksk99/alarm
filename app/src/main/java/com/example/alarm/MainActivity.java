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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

//    private ArrayList<String> channelID;        //channelId는 설정시간 + 할일 {(calendar.get(Calendar.HOUR)+"시 "+calendar.get(Calendar.MINUTE)+"분") + (content)} 로 통일
//    private ArrayList<Integer> requestCode;     //requestCode는 설정시간+할일 {(int)calendar.getTimeInMillis()+content.hashCode()} 로 통일
    private NotificationManager notificationManager;
    private FileOutputStream channelIdFOS;
    private FileOutputStream requestCodeFOS;
    private FileInputStream channelIdFIS;
    private FileInputStream requestCodeFIS;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private EditText editText;
    private Button delButton;
    private Spinner spinner;
    String[] items = {"일요일","월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openWriteFile();
//        channelID = new ArrayList<String>();
//        requestCode = new ArrayList<Integer>();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        delButton = findViewById(R.id.delete);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예약된 알림을 개대충 삭제함
                //PedingIntent.getBroadcast의 두번째 인자가 중요함 마지막도 중요함 (알림 생성할때랑 똑같이 해야됨)
//                alarmManager.cancel(PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
                Intent manageIntent = new Intent(MainActivity.this, ManageActivity.class);
//                manageIntent.putExtra("channelID",channelID);
//                manageIntent.putExtra("requestCode",requestCode);
                startActivity(manageIntent);
            }
        });
        setTitle("박성규 맞춤 알람");
        Button alarm = findViewById(R.id.alarm);
        spinner = findViewById(R.id.spinner);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);   //12시간 표기방법에서 24시간 표기방법으로 바꿈
        editText = findViewById(R.id.editText);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//        class AlarmRunnable implements Runnable {
//
//            @Override
//            public void run() {
//                setAlarm(getTime());
//            }
//        }
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
    private void openWriteFile(){
        try {
            channelIdFOS = openFileOutput(getString(R.string.channel_id_file), Context.MODE_APPEND);
            requestCodeFOS = openFileOutput(getString(R.string.request_code_file), Context.MODE_APPEND);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setAlarm(Calendar calendar) {

        String contentTitle = calendar.get(Calendar.HOUR)+"시 "+calendar.get(Calendar.MINUTE)+"분";
        String content = editText.getText().toString();
        Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
//        channelID.add(contentTitle+content);
        writeIdFile(contentTitle+content);
//        requestCode.add((int)calendar.getTimeInMillis()+content.hashCode());
        writeRequestCodeFile((int)calendar.getTimeInMillis()+content.hashCode());
        receiverIntent.putExtra("contentTitle", contentTitle);
        receiverIntent.putExtra("contentText", content);
        receiverIntent.putExtra("time",(int)calendar.getTimeInMillis());
        receiverIntent.putExtra("dayOfWeek", calendar.get(Calendar.DAY_OF_WEEK));
        int notiSize = getNotiSize();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, readRequestCodeFile(notiSize), receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT); //마지막 인자 : receiverIntent의 Extras 값을 최신으로 유지하게 함
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*10/*AlarmManager.INTERVAL_DAY*7*/, pendingIntent); //알림 반복 설정 : 세번째 인자가 반복 주기
    }
    private int getNotiSize(){
        int i=0;
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(getString(R.string.request_code_file)));
            while((buffer.readLine())!=null){
                i++;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return i;
    }
    private void writeIdFile(String text){
        try {
            channelIdFOS.write((text+"\n").getBytes());
//            channelIdFOS.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void writeRequestCodeFile(Integer text){
        try{
            String str = text.toString() + "\n";
            requestCodeFOS.write(str.getBytes());
//            requestCodeFOS.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private int readRequestCodeFile(int index){
        String line = null;
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(getString(R.string.request_code_file)));
            for (int i = 0; i <= index; i++) {
                line = buffer.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(line);
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
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        int getDayOfWeek = 0 ;
        String selectedItemName = spinner.getSelectedItem().toString();

        for (int i = 0; i < 7; i++) {
            if (items[i].equals(selectedItemName))
                getDayOfWeek =i;
        }
        getDayOfWeek += 1;
        calendar.set(Calendar.DAY_OF_WEEK,getDayOfWeek);

        return calendar;
    }
}