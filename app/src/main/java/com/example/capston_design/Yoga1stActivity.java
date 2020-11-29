package com.example.capston_design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Yoga1stActivity extends AppCompatActivity {

    private ArrayList<YogaItem> firstlist =new ArrayList<>();
    RecyclerView listview;
    YogaAdapter adapter;

    private String YOGA1st_URL = "http://13.125.245.6:5000/getYogas?trimester=1st";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga1st);

        listview=findViewById(R.id.yoga1st_listview);


        adapter = new YogaAdapter(firstlist,Yoga1stActivity.this);
        listview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(Yoga1stActivity.this,LinearLayoutManager.VERTICAL,false);
        listview.setLayoutManager(layoutManager);

        new GetYoga1st().execute();

        adapter.setOnItemClicklistener(new OnPersonItemClickListener_yoga() {
            @Override
            public void onItemClick(YogaAdapter.VH holder, View view, int position) {
                YogaItem item = adapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putString("pose",item.getPose());
                bundle.putString("path",item.getPath());

                Intent intent = new Intent(Yoga1stActivity.this,YogainfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private class GetYoga1st extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(YOGA1st_URL);

            Log.e("json",jsonStr);

            if (jsonStr!= null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray array = jsonObject.getJSONArray("res_data");

                    for (int i=0; i<array.length();i++){
                        JSONObject c =array.getJSONObject(i);

                        String pose = c.getString("pose");
                        String path = c.getString("path");

                        firstlist.add(new YogaItem(pose,path));
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

            adapter = new YogaAdapter(firstlist,Yoga1stActivity.this);
            listview.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(Yoga1stActivity.this,LinearLayoutManager.VERTICAL,false);
            listview.setLayoutManager(layoutManager);

        }
    }
}