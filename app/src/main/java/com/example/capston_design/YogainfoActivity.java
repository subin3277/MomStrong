package com.example.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YogainfoActivity extends AppCompatActivity {

    String pose,description,path;

    private String YOGAINFO_URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yogainfo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        pose = bundle.getString("pose");

        YOGAINFO_URL=YOGAINFO_URL+"&pose="+pose;



    }

    private class GetYogainfo extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(YOGAINFO_URL);

            if (jsonStr!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("res_data");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject c= jsonArray.getJSONObject(i);

                        description=c.getString("description");
                        path = c.getString("path");

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



        }

    }


}