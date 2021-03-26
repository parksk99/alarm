package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {
    Button button;
    ContactDBHelper dbHelper;
    TextView textView;
    LinearLayout linearLayout;
    SQLiteDatabase alarmDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        linearLayout = (LinearLayout)findViewById(R.id.manage_layout);

        alarmDatabase = this.openOrCreateDatabase("contact.db", MODE_PRIVATE, null);
        textView = setTextView();
        dbHelper = new ContactDBHelper(this);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(textView.getText().toString());
            }
        });
    }

    //알람을 삭제
    private void cancel(String key){
        Intent intent = new Intent(ManageActivity.this, AlarmReceiver.class);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        int requestCode = getRequestCode(key);
        am.cancel(PendingIntent.getBroadcast(ManageActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        alarmDatabase.execSQL(ContactDBCtrict.SQL_DELETE+" WHERE "+ContactDBCtrict.COL_CONTENT_TITLE+" = '"+textView.getText().toString()+"'");
    }

    //database를 읽어옴
    private Cursor getCursor(){
        return alarmDatabase.rawQuery(ContactDBCtrict.SQL_SELECT, null);
    }

    //textView에 db내용을 표시함
    private TextView setTextView(){
        TextView tmpTextView = new TextView(this);;
        Cursor cursor = getCursor();
        if(cursor.moveToNext()) {
            tmpTextView.setText(cursor.getString(0));
            cursor.close();
        }
        else{
            tmpTextView.setText("there is no alarm");
        }
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tmpTextView.setLayoutParams(parms);
        linearLayout.addView(tmpTextView);
        return tmpTextView;
    }

    //db에서 원하는 time과 content를 가져와서 requestCode를 조합함
    private int getRequestCode(String contentTitle){
        // + " WHERE " + COL_CONTENT_TITLE + " = ";
        int tmp = -1;
        Cursor cursor = alarmDatabase.rawQuery(ContactDBCtrict.SQL_SELECT+" WHERE "+ContactDBCtrict.COL_CONTENT_TITLE+" = '"+contentTitle+"'", null);
        if(cursor.moveToNext()) {
            tmp = cursor.getInt(2) + cursor.getString(1).hashCode();
            cursor.close();
        }
        return tmp;
    }
}