package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        Intent intent = getIntent();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                ArrayList<Integer> arrayList = intent.getIntegerArrayListExtra("requestCode");
                for(int i=0; i<arrayList.size(); i++) {
                    am.cancel(PendingIntent.getBroadcast(ManageActivity.this, arrayList.get(i), new Intent(ManageActivity.this, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
                    //arrayList의 0번째 요소도 지워야 하는데 여기서 지우면 원래 있던 애도 지워지나?
                }
            }
        });
    }
}