package com.lck.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class AddcalActivity extends AppCompatActivity {

    CheckBox box10,box20,box30,box60,box24;
    EditText text;
    TextView date,date2;
    Button add, cancel, set, visible;
    DatePicker datePicker;
    TimePicker timePicker;
    Context context;
    AlarmManager alarm_manager;
    PendingIntent pendingIntent;
    String txtyear,txtmon,txtdat,txthour,txtmin;

    String year,month,day,setdate,todaydate;
    int y,m,d;

    private String CALPOST_URL="http://13.125.245.6:3000/api/calendars/postCalendars";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcal);

        box10 = findViewById(R.id.addcal_cb_10);
        box20 = findViewById(R.id.addcal_cb_20);
        box30 = findViewById(R.id.addcal_cb_30);
        box60 = findViewById(R.id.addcal_cb_60);
        box24 = findViewById(R.id.addcal_cb_24);

        text = findViewById(R.id.addcal_et_text);
        date = findViewById(R.id.addcal_et_date);
        add = findViewById(R.id.addcal_add_btn);
        cancel = findViewById(R.id.addcal_can_btn);
        visible = findViewById(R.id.dialog_date_btn);
        datePicker = findViewById(R.id.dialog_datepicker);
        timePicker = findViewById(R.id.dialog_timepicker);
        set = findViewById(R.id.dialog_set_button);
        date2=findViewById(R.id.dialog_date);

        final Calendar calendar = Calendar.getInstance();
        this.context=this;

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar_alarm = Calendar.getInstance();
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        todaydate = calendar.get(Calendar.YEAR)+"년 "+(calendar.get(Calendar.MONTH)+1)+"월 "+calendar.get(Calendar.DAY_OF_MONTH)+"일 "
                +calendar.get(Calendar.HOUR_OF_DAY)+"시 "+calendar.get(Calendar.MINUTE)+"분 ";
        date.setText(todaydate);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                set.setVisibility(View.VISIBLE);
            }
        });


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtyear = datePicker.getYear()+"";
                String txtmon = (datePicker.getMonth()+1)+"";
                String txtday =datePicker.getDayOfMonth()+"";
                String txthour = timePicker.getHour()+"";
                String txtmin = timePicker.getMinute()+"";

                if (datePicker.getMonth()<9){
                    txtmon=0+txtmon;
                }
                if (timePicker.getMinute()<10){
                    txtmin=0+txtmin;
                }
                if (timePicker.getHour()<10){
                    txthour=0+txthour;
                }
                setdate=txtyear+"-"+txtmon+"-"+txtday+"T"+txthour+":"+txtmin+":00.000Z";
                date2.setText(setdate);
                date.setText(txtyear+"년 "+txtmon+"월"+txtday+"일 "+txthour+"시"+txtmin+"분");

                set.setVisibility(View.GONE);
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.GONE);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // 일정 서버에 등록
                String txtdate = text.getText().toString();
                String txtcomment =date2.getText().toString();

                JSONObject addcal = new JSONObject();
                try {
                    addcal.put("users_id",MainActivity.user_id);
                    addcal.put("date",txtdate);
                    addcal.put("content",txtcomment);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("json",addcal.toString());
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                InputStream inputStream = null;
                ByteArrayOutputStream baos=null;

                try {
                    URL URL = new URL(CALPOST_URL);
                    connection=(HttpURLConnection) URL.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Cache-Control","no-cache");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("Accept","application/json");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    outputStream = connection.getOutputStream();
                    outputStream.write(addcal.toString().getBytes());
                    outputStream.flush();

                } catch (IOException e){
                    e.printStackTrace();
                }
/*
                //알림 등록
                calendar_alarm.set(Calendar.YEAR,datePicker.getYear());
                calendar_alarm.set(Calendar.MONTH,datePicker.getMonth());
                calendar_alarm.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                calendar_alarm.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar_alarm.set(Calendar.MINUTE,timePicker.getMinute());


                my_intent.putExtra("state","alarm on");
                pendingIntent = PendingIntent.getBroadcast(AddcalActivity.this,0,my_intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar_alarm.getTimeInMillis(),pendingIntent);
                Log.e("알람",datePicker.getYear()+"년"+(datePicker.getMonth()+1)+"월"+datePicker.getDayOfMonth()+"일"+
                        timePicker.getHour()+"시"+timePicker.getMinute()+"분");

                //finish();
                */
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}