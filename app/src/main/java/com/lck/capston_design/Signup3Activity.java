package com.lck.capston_design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Signup3Activity extends AppCompatActivity {

    static ProgressBar progressBar;
    static TextView progresstext;
    RecyclerView recyclerView;

    Signup3Adapter adapter;
    ArrayList<Signup3dietItem> items = new ArrayList<>();

    Button button;
    ArrayList<String> selectlist = new ArrayList<>();
    int selectcount = 0;

    JSONArray jsonArray = new JSONArray();

    private String GETALLDIETS_URL = "http://13.125.245.6:3000/api/diets/getAllDiets";
    private String SelectDiets_URL = "http://13.125.245.6:3000/api/diets/patchSelectedDietsRatingsInit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        progressBar = findViewById(R.id.signup3_progressbar);
        progresstext = findViewById(R.id.signup3_progresstext);
        recyclerView = findViewById(R.id.signup3_recyclerview);
        button = findViewById(R.id.signup3_btn);

        Signup3Adapter.count = 0;

        items.clear();

        adapter = new Signup3Adapter(items, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        GetDiet();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Signup3Adapter.count < 10){
                    Toast.makeText(getApplicationContext(),"10개 이상 선택해 주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    for (Signup3dietItem item : items){
                        if (item.isSelected()){
                            jsonArray.put(Integer.parseInt(item.idx));

                        }
                    }
                    Log.e("selectlist :",selectlist.toString());

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("users_idx",Signup2Activity.users_idx);
                        jsonObject.put("diets_idx",jsonArray);


                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.e("json : ",jsonObject.toString());

                    Toast.makeText(getApplicationContext(),"선택이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup3Activity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in_left,R.anim.out_right);
                }
            }
        });
    }

    void GetDiet(){

        HttpHandler sh = new HttpHandler();

        String jsonStr = sh.makeServiceCall(GETALLDIETS_URL);

        if (jsonStr !=null){
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                JSONArray array = jsonObject.getJSONArray("res_data");

                for (int i=0; i< array.length();i++){
                    JSONObject c =array.getJSONObject(i);

                    String idx = c.getString("idx");
                    String name = c.getString("dietName");

                    items.add(new Signup3dietItem(idx,name));
                }
            } catch (JSONException e){
                e.printStackTrace();
                Log.e("error", e.toString());
            }
        }
    }

    private class SelectDiets extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos=null;
            String response = null;

            try {
                URL URL = new URL(SelectDiets_URL);
                connection=(HttpURLConnection) URL.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control","no-cache");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("users_idx",1);
                    jsonObject.put("diets_idx",selectlist);


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
                        Toast.makeText(Signup3Activity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup3Activity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_left,R.anim.out_right);
                        finish();
                        break;
                    default:
                        Toast.makeText(Signup3Activity.this, "정보 전송에 실패했습니다..", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
}