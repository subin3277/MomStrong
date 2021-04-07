package com.lck.capston_design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InformationActivity extends AppCompatActivity {

    LineChart graphView;
    RecyclerView listView;
    TextView tv_expectedDate;
    Button btn_change;
    Context context=this;
    WeightAdapter adapter;
    DatePickerDialog datePickerDialog;
    ReturnDate returnDate;
    String year,month,day;
    int y,m,d;

    String weight;

    ArrayList<WeightItem> weight_list = new ArrayList<WeightItem>();

    private String WEIGHT_URL = "";
    private String DATE_URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        graphView=findViewById(R.id.info_graph);
        listView = findViewById(R.id.info_table);
        tv_expectedDate = findViewById(R.id.info_tv_expectedDate);
        btn_change = findViewById(R.id.info_btn_change);

        returnDate = new InformationActivity.ReturnDate();

        year = returnDate.returnYear();
        month = returnDate.returnMonth();
        day = returnDate.returnDay();

        adapter = new WeightAdapter(weight_list,InformationActivity.this);
        listView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(InformationActivity.this,LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(layoutManager);

        new GetGraph();

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(InformationActivity.this, listener, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePickerDialog.show();
            }

        });
    }

    public class PostDate extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos=null;
            String response = null;

            try {
                URL URL = new URL(DATE_URL);
                connection=(HttpURLConnection) URL.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control","no-cache");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                JSONObject jsonObject = new JSONObject();

                /*try {/jsonObject.put("id",id);
                    jsonObject.put("pw",password);


                } catch (JSONException e){
                    e.printStackTrace();
                }*/
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

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            tv_expectedDate.setText(weight);
/*

            try {
                JSONObject responseJSON = new JSONObject(s);
                String result = (String) responseJSON.get("res_state");
                String msg = (String) responseJSON.get("res_msg");


            } catch (JSONException e){
                e.printStackTrace();
            }
*/

        }
    }

    public class GetGraph extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(WEIGHT_URL);

            Log.e("json",jsonStr);

            if (jsonStr!= null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray array = jsonObject.getJSONArray("res_data");

                    for (int i=0; i<array.length();i++){
                        JSONObject c =array.getJSONObject(i);

                        String date = c.getString("date");
                        String weight = c.getString("weight");
                        String ect = c.getString("ect");

                        weight_list.add(new WeightItem(date,weight,ect));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();

            for (int i=0;i<weight_list.size();i++){
                String weight_date = weight_list.get(i).getDate();
                String weight_weight = weight_list.get(i).getWeight();

                entries.add(new Entry(Float.parseFloat(weight_weight),i));
                labels.add(weight_date);
            }

            LineDataSet dataSet = new LineDataSet(entries,"");
            LineData data = new LineData((ILineDataSet) labels,dataSet);
            graphView.setData(data);

        }
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

            tv_expectedDate.setText(year+"-"+month+"-"+day);
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
}