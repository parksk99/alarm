package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity{
//    Button button;
    ContactDBHelper dbHelper;
    ArrayList<TextView> textViewArrayList;
    ArrayList<Button> buttonList;
//    TextView textView;
    LinearLayout linearLayout;
    ArrayList<LinearLayout> linearLayoutList;
    SQLiteDatabase alarmDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        linearLayout = (LinearLayout)findViewById(R.id.manage_layout);

        alarmDatabase = this.openOrCreateDatabase("contact.db", MODE_PRIVATE, null);
        textViewArrayList = setTextView();
        dbHelper = new ContactDBHelper(this);
       // button = findViewById(R.id.button);

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(ManageActivity.this, "delete", Toast.LENGTH_SHORT);
                toast.show();
                int num = (int)v.getTag();
                cancel(textViewArrayList.get(num).getText().toString(), num);
            }
        };
        for(int i=0; i< buttonList.size(); i++){
            buttonList.get(i).setOnClickListener(onClickListener);
        }

    }


    //알람을 삭제
    private void cancel(String key, int index){

        Intent intent = new Intent(ManageActivity.this, AlarmReceiver.class);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        int requestCode = getRequestCode(key);
        am.cancel(PendingIntent.getBroadcast(ManageActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));

       alarmDatabase.execSQL(ContactDBCtrict.SQL_DELETE+" WHERE "+ContactDBCtrict.COL_CONTENT_TITLE+" = '"+textViewArrayList.get(index).getText().toString()+"'");
//       linearLayoutList.get(index).removeView(textViewArrayList.get(index));
//       linearLayoutList.get(index).removeView(buttonList.get(index));
       linearLayout.removeView(linearLayoutList.get(index));

       textViewArrayList.remove(index);
       buttonList.remove(index);
       linearLayoutList.remove(index);
    }

    //database를 읽어옴
    private Cursor getCursor(){
        return alarmDatabase.rawQuery(ContactDBCtrict.SQL_SELECT, null);
    }

    private int getCount(){
        Cursor c = getCursor();
        int i = c.getCount();
        c.close();
        return i;
    }

    //textView에 db내용을 표시함
    private ArrayList<TextView> setTextView(){
        textViewArrayList = new ArrayList<>();
        int count = getCount();
        linearLayoutList = new ArrayList<>();
        buttonList = new ArrayList<>();
        Cursor cursor = getCursor();
        for(int i=0; i<count; i++){
            linearLayoutList.add(new LinearLayout(this));
            buttonList.add(new Button(this));
            textViewArrayList.add(new TextView(this));
            if(cursor.moveToNext()){
                linearLayoutList.get(i).setOrientation(LinearLayout.HORIZONTAL);

                textViewArrayList.get(i).setText(cursor.getString(0)/* + "\n"+cursor.getString(1)*/);

                buttonList.get(i).setText("삭제");
                buttonList.get(i).setTag(i);
//                textViewArrayList.get(i).setText("\n"+cursor.getString(1));
            }
            else{
                textViewArrayList.get(i).setText("There is no alarm");
                return null;
            }


            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewArrayList.get(i).setLayoutParams(parms);
            buttonList.get(i).setLayoutParams(parms);
//            textViewArrayList.get(i).setTag(i);

            linearLayoutList.get(i).addView(textViewArrayList.get(i));
            linearLayoutList.get(i).addView(buttonList.get(i));
            linearLayout.addView(linearLayoutList.get(i));
        }
        return textViewArrayList;
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