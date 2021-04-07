package com.lck.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context =this;

    TextView updatedate;
    DatePickerDialog datePickerDialog;
    ReturnDate returnDate;
    String year,month,day;
    int y,m,d;
    String txtdate;

    Button end,logout;

    String UPDATE_URL = "http://13.125.245.6:3000/api/users/updateExpectedDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        end =findViewById(R.id.setting_end);
        logout = findViewById(R.id.setting_logout);
        updatedate = findViewById(R.id.setting_expecteddate);
        returnDate = new ReturnDate();

        year = returnDate.returnYear();
        month = returnDate.returnMonth();
        day = returnDate.returnDay();

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = (DrawerLayout) findViewById(R.id.setting_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id==R.id.tab_eat){
                    Intent intent = new Intent(SettingActivity.this, DietActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id==R.id.tab_yoga){
                    Intent intent = new Intent(SettingActivity.this, YogaActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id==R.id.tab_hos){
                    Intent intent = new Intent(SettingActivity.this, HospitalActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id == R.id.tab_logout){
                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = auto.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                }
                else if (id == R.id.tab_set){
                    Intent intent = new Intent(SettingActivity.this,SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id==R.id.tab_findhos){
                    Intent intent = new Intent(SettingActivity.this,FindhospitalActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        updatedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(SettingActivity.this, listener, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePickerDialog.show();
            }

        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtdate = updatedate.getText().toString();
                new endbutton().execute();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            y = i;
            m = i1 + 1;
            d = i2;
            year = String.valueOf(y);
            month = String.valueOf(m);
            day = String.valueOf(d);
            if (m<10){
                month="0"+month;
            }
            if (d<10){
                day = "0"+day;
            }



            updatedate.setText(year+"-"+month+"-"+day);
            Log.e("date",year+" - "+month+" - "+day);
        }
    };

    public class ReturnDate{
        long now;
        Date date;
        SimpleDateFormat sdf;
        ReturnDate(){
            now=System.currentTimeMillis();
            date=new Date(now);
        }
        public String returnYear(){
            sdf = new SimpleDateFormat("yyyy");
            String year = sdf.format(date);
            return year;
        }
        public String returnMonth(){
            sdf = new SimpleDateFormat("MM");
            String month = sdf.format(date);
            return month;
        }
        public String returnDay(){
            sdf = new SimpleDateFormat("dd");
            String day = sdf.format(date);
            return day;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class endbutton extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos=null;
            String response = null;

            try {
                URL URL = new URL(UPDATE_URL);
                connection=(HttpURLConnection) URL.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control","no-cache");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("id",MainActivity.user_id);
                    jsonObject.put("expectedDate",txtdate+"T00:00:00.000Z");

                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.e("json","생성한 json"+jsonObject.toString());

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray(); response = new String(byteData);

                    response = new String(byteData);

                    Log.e("response",response);

                }

            } catch (IOException e){
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject responseJSON = new JSONObject(s);
                String result = (String) responseJSON.get("res_state");
                String msg = (String) responseJSON.get("res_msg");

                switch (result) {
                    case "success":
                        Toast.makeText(SettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        Toast.makeText(SettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
}