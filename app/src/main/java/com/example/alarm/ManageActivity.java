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
import android.widget.TextView;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {
    Button button;
    ContactDBHelper dbHelper;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        textView = setTextView();
        dbHelper = new ContactDBHelper(this);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, AlarmReceiver.class);
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                am.cancel(PendingIntent.getBroadcast(ManageActivity.this, getRequestCode(textView.getText().toString()), intent, PendingIntent.FLAG_UPDATE_CURRENT));
                //arrayList의 0번째 요소도 지워야 하는데 여기서 지우면 원래 있던 애도 지워지나?
            }
        });
    }
    private Cursor getCursor(){
        SQLiteDatabase alarmDatabase = dbHelper.getReadableDatabase();
        return alarmDatabase.rawQuery(ContactDBCtrict.SQL_SELECT, null);
    }
    private TextView setTextView(){
        TextView tmpTextView = new TextView(this);
        Cursor cursor = getCursor();
        tmpTextView.setText(cursor.getString(0));
        return tmpTextView;
    }
    private int getRequestCode(String contentTitle){
        SQLiteDatabase alarmDatabase = dbHelper.getReadableDatabase();
        // + " WHERE " + COL_CONTENT_TITLE + " = ";
        Cursor cursor = alarmDatabase.rawQuery(ContactDBCtrict.SQL_SELECT+" WHERE "+ContactDBCtrict.COL_CONTENT_TITLE+" = "+contentTitle, null);
        return cursor.getInt(2) + cursor.getString(1).hashCode();
    }
}