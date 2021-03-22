package com.example.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HospitalActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context =this;
    Button findhosp, addcal;
    ArrayList<HashMap<String,String>> cal_list = new ArrayList<>();
    ArrayList<CalendarDay> dates=new ArrayList<>();
    MaterialCalendarView CalendarView;
    ListView listView;
    JSONArray cal_array = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        findhosp = findViewById(R.id.hosp_btn_findhos);
        addcal = findViewById(R.id.hosp_btn_add);
        listView = findViewById(R.id.hosp_calview);

        CalendarView = findViewById(R.id.hosp_calendar);

        CalendarView.setSelectedDate(CalendarDay.today());
        CalendarView.addDecorators(new SundayDecorator(),new SaturdayDecorator());

        //calendar.set(2020,10,27);
        //CalendarDay day = CalendarDay.from(calendar);
        // dates.add(day);
        //CalendarView.addDecorator(new EventDecorator(Color.RED,dates));

        new GetCal().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.hosp_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = (DrawerLayout) findViewById(R.id.hosp_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

               if (id==R.id.tab_eat){
                    Intent intent = new Intent(HospitalActivity.this, DietActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id==R.id.tab_yoga){
                    Intent intent = new Intent(HospitalActivity.this, YogaActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id==R.id.tab_hos){
                    Intent intent = new Intent(HospitalActivity.this, HospitalActivity.class);
                    startActivity(intent);
                    finish();

                }
                return true;
            }
        });

        addcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HospitalActivity.this,AddcalActivity.class);
                startActivity(intent);

/*
                final AddCalendarDialog dialog = new AddCalendarDialog(HospitalActivity.this, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {

                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_addcal);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(params);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        TextView date = dialog.findViewById(R.id.dialog_date);
                        TextView comment = dialog.findViewById(R.id.addcal_et_text);

                        String txtdate = date.getText().toString();
                        String txtcomment = comment.getText().toString();

                        JSONObject addcal = new JSONObject();
                        try {
                            addcal.put("date",txtdate);
                            addcal.put("comment",txtcomment);
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

                    }
                });*/
            }
        });

        CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                Log.e("claview","실행");

                ArrayList<HashMap<String,String>> cal_list2 = new ArrayList<>();

                for (int i=0;i<cal_list.size();i++){
                    Set key = cal_list.get(i).keySet();
                    String txtkey=key.toString().replace("[","");
                    String realkey = key.toString();
                    Log.e("cla_list",key.toString());
                    Log.e("cla_list",cal_list.get(i).toString());

                    String[] contentarray = cal_list.get(i).toString().split("=");
                    String txtcontent = contentarray[1].replace("}","");

                    String[] dayarray = txtkey.split("-");
                    int year = Integer.parseInt(dayarray[0]);
                    int month = Integer.parseInt(dayarray[1]);
                    String[] dayarray2= dayarray[2].split("T");
                    int day = Integer.parseInt(dayarray2[0]);
                    String[] time = dayarray2[1].split(":");
                    String hour=time[0];
                    String min = time[1];


                    if ((year==date.getYear())&&(month-1==date.getMonth())&&(day==date.getDay())){
                        HashMap<String, String> hash_content = new HashMap<>();
                        hash_content.put("date",year+"-"+month+"-"+day);
                        hash_content.put("time",hour+":"+min);
                        hash_content.put("content",txtcontent);

                        cal_list2.add(hash_content);
                        Log.e("cla_list",cal_list2.toString());
                    }
                }


                ListAdapter content_adapter = new SimpleAdapter(context,cal_list2,R.layout.hosp_calcontent_list
                        ,new String[]{"date","time","content"}
                        , new int[]{R.id.hosplist_date,R.id.hosplist_time,R.id.hosplist_content});
                listView.setAdapter( content_adapter);


            }
        });

        findhosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalActivity.this,FindhospitalActivity.class);
                startActivity(intent);
            }
        });
    }


    private class GetCal extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids){
            HttpHandler cal = new HttpHandler();

            String GETCAL_URL = "http://13.125.245.6:3000/api/calendars/getCalendars?users_id=";
            String cal_json = cal.makeServiceCall(GETCAL_URL+MainActivity.user_id);
            if (cal_json !=null){
                try {
                    JSONObject jsonObject = new JSONObject(cal_json);

                    cal_array=new JSONArray();
                    cal_array = jsonObject.getJSONArray("res_data");
                    Log.e("cal",cal_array.toString());
                    for (int i=0;i<cal_array.length();i++){
                        JSONObject data = cal_array.getJSONObject(i);

                        String id=data.getString("users_id");
                        String date = data.getString("date");
                        String content = data.getString("content");

                        HashMap<String,String> cal_contact = new HashMap<>();
                        cal_contact.put(date,content);
                        cal_list.add(cal_contact);

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            Calendar calendar = Calendar.getInstance();

            for (int i=0;i<cal_list.size();i++){
                Set key = cal_list.get(i).keySet();
                String txtkey=key.toString().replace("[","");

                String[] dayarray = txtkey.split("-");
                int year = Integer.parseInt(dayarray[0]);
                int month = Integer.parseInt(dayarray[1])-1;
                String[] dayarray2= dayarray[2].split("T");
                int day = Integer.parseInt(dayarray2[0]);

                Log.e("cal2",String.valueOf(year)+String.valueOf(month)+String.valueOf(day));

                calendar.set(year,month,day);
                CalendarDay calendarDay = CalendarDay.from(calendar);
                dates.add(calendarDay);
            }

            CalendarView.addDecorator(new EventDecorator(Color.RED,dates));

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


    class SaturdayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day){
            day.copyTo(calendar);
            int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }
        @Override
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }
    class SundayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day){
            day.copyTo(calendar);
            int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }
        @Override
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }
    public class EventDecorator implements DayViewDecorator{
        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates){
            this.color=color;
            this.dates=new HashSet<>(dates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day){
            return dates.contains(day);
        }
        @Override
        public void decorate(DayViewFacade view){
            view.addSpan(new DotSpan(5,color));
        }
    }
}