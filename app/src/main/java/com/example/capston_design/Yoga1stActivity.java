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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Yoga1stActivity extends AppCompatActivity {

    private ArrayList<YogaItem> firstlist =new ArrayList<>();
    RecyclerView listview;
    YogaAdapter adapter;
    Button button;
    TextView title;
    static String yoga_idx;

    private String YOGA1st_URL = "http://13.125.245.6:3000/api/yogas/getYogas?trimester=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga1st);

        listview=findViewById(R.id.yoga1st_listview);
        button = findViewById(R.id.yoga1st_button);
        title = findViewById(R.id.yoga1st_title);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        yoga_idx = bundle.getString("yoga_idx");

        if (yoga_idx.equals("1st")){
            title.setText("요가(초기)");
        }
        else if (yoga_idx.equals("2nd")){
            title.setText("요가(중기)");
        }
        else {
            title.setText("요가(말기)");
        }

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Yoga1stActivity.this,CameraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("yoga_idx",yoga_idx);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_left,R.anim.out_right);
                finish();
            }
        });
    }

    private class GetYoga1st extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(YOGA1st_URL+yoga_idx);

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