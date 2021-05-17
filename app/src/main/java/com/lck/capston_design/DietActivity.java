package com.lck.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonIOException;

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

public class DietActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context = this;

    static RecyclerView recyclerViewrice,recyclerViewsoup,recyclerViewfirst,recyclerViewsecond;
    static DietAdapter dietAdapter_rice,dietAdapter_soup,dietAdapter_first,dietAdapter_second;
    ItemTouchHelper helper;
    static DietItem riceitem, soupitem,firstitem,seconditem;
    static String ricename,soupname,firstname,secondname;
    Button finish1,finish2,finish3,finish4;
    TextView soup,first,second,tvnull;
    static JSONArray riceresult,firstresult,secondresult;

    static ArrayList<String> ricelist = new ArrayList<>();
    static ArrayList<String> souplist = new ArrayList<>();
    static ArrayList<String> firstlist = new ArrayList<>();
    static ArrayList<String> secondlist = new ArrayList<>();

    static String RICE_URL = "http://13.125.245.6:3000/api/diets/postRiceDiets";
    static String SOUP_URL = "http://13.125.245.6:3000/api/diets/postSoupDiets";
    static String SIDE1_URL= "http://13.125.245.6:3000/api/diets/postSideDiets1";
    static String SIDE2_URL= "http://13.125.245.6:3000/api/diets/postSideDiets2";
    String FINISH_URL="http://13.125.245.6:3000/api/diets/patchSelectedDietsRatings";

    static ArrayList<DietItem> side1list = new ArrayList<>();
    static ArrayList<DietItem> side2list = new ArrayList<>();
    static int side1index = 0;
    static int side2index = 0;
    static int firstlength = 0;
    static int secondlength = 0;
    static JSONObject ricejson,soupjson,firstjson,secondjson;

    ArrayList<Integer> finshlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.diet_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = findViewById(R.id.diet_drawer_layout);
        recyclerViewrice = findViewById(R.id.diet_listview_rice);
        recyclerViewsoup = findViewById(R.id.diet_listview_soup);
        recyclerViewfirst = findViewById(R.id.diet_listview_first);
        recyclerViewsecond = findViewById(R.id.diet_listview_second);
        finish1 = findViewById(R.id.diet_btn_next1);
        finish2 = findViewById(R.id.diet_btn_next2);
        finish3 = findViewById(R.id.diet_btn_next3);
        finish4 = findViewById(R.id.diet_btn_finish);
        soup = findViewById(R.id.diet_tv_soup);
        first = findViewById(R.id.diet_tv_first);
        second = findViewById(R.id.diet_tv_second);

        tvnull = findViewById(R.id.dietlist_null);

        ricelist = new ArrayList<>();
        souplist = new ArrayList<>();
        firstlist = new ArrayList<>();
        secondlist = new ArrayList<>();
        side1index=0;
        side2index=0;

        NavigationView navigationView = (NavigationView) findViewById(R.id.diet_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id == R.id.tab_eat) {
                    Intent intent = new Intent(DietActivity.this, DietActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.tab_yoga) {
                    Intent intent = new Intent(DietActivity.this, YogaActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.tab_hos) {
                    Intent intent = new Intent(DietActivity.this, HospitalActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.tab_logout){
                    Intent intent = new Intent(DietActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = auto.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                }
                else if (id == R.id.tab_set){
                    Intent intent = new Intent(DietActivity.this,SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id==R.id.tab_findhos){
                    Intent intent = new Intent(DietActivity.this,FindhospitalActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });



        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewrice.setLayoutManager(manager);

        dietAdapter_rice = new DietAdapter();
        recyclerViewrice.setAdapter(dietAdapter_rice);

        helper = new ItemTouchHelper(new ItemTouchHelperCallback(dietAdapter_rice));
        helper.attachToRecyclerView(recyclerViewrice);

        new GetRice().execute();

        finish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soup.setVisibility(View.VISIBLE);
                recyclerViewsoup.setVisibility(View.VISIBLE);
                finish2.setVisibility(View.VISIBLE);

                helper.attachToRecyclerView(null);

                finshlist.add(riceitem.idx);

                LinearLayoutManager manager = new LinearLayoutManager(DietActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewsoup.setLayoutManager(manager);

                dietAdapter_soup = new DietAdapter();
                recyclerViewsoup.setAdapter(dietAdapter_soup);

                helper = new ItemTouchHelper(new ItemTouchHelperCallback_soup(dietAdapter_soup));
                helper.attachToRecyclerView(recyclerViewsoup);

                new GetSoup().execute();

                finish1.setClickable(false);

            }
        });


        finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first.setVisibility(View.VISIBLE);
                recyclerViewfirst.setVisibility(View.VISIBLE);
                finish3.setVisibility(View.VISIBLE);

                helper.attachToRecyclerView(null);

                finshlist.add(soupitem.idx);

                LinearLayoutManager manager = new LinearLayoutManager(DietActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewfirst.setLayoutManager(manager);

                dietAdapter_first = new DietAdapter();
                recyclerViewfirst.setAdapter(dietAdapter_first);

                helper = new ItemTouchHelper(new ItemTouchHelperCallback_first(dietAdapter_first));
                helper.attachToRecyclerView(recyclerViewfirst);

                new GetSide1().execute();

                finish2.setClickable(false);

            }
        });

        finish3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                second.setVisibility(View.VISIBLE);
                recyclerViewsecond.setVisibility(View.VISIBLE);
                finish4.setVisibility(View.VISIBLE);

                helper.attachToRecyclerView(null);

                finshlist.add(firstitem.idx);

                LinearLayoutManager manager = new LinearLayoutManager(DietActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewsecond.setLayoutManager(manager);

                dietAdapter_second = new DietAdapter();
                recyclerViewsecond.setAdapter(dietAdapter_second);

                helper = new ItemTouchHelper(new ItemTouchHelperCallback_second(dietAdapter_second));
                helper.attachToRecyclerView(recyclerViewsecond);

                new GetSide2().execute();
                finish3.setClickable(false);

            }
        });

        finish4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finshlist.add(seconditem.idx);
                new PostDiet().execute();

            }
        });
    }


    private void setUpRecyclerView() {
        recyclerViewrice.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                helper.onDraw(c, parent, state);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static class GetRice extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String response = null;
            JSONObject jsonObject_rice = new JSONObject();

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos = null;

            try {
                URL url = new URL(RICE_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    //userid 추가(모든 post에서)
                    jsonObject_rice.put("users_age",MainActivity.user_age);
                    jsonObject_rice.put("expectedDate","2021-09-06T00:00:00.000Z");
                    jsonObject_rice.put("swipeRice", ricelist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("json",jsonObject_rice.toString());

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject_rice.toString().getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);

                    Log.e("response",response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            JSONObject result;
            String msg = null, nutrition = null;

            int idx=0;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONObject) responseJSON.get("res_data");
                ricejson = (JSONObject) result.get("Rice");
                msg = ricejson.getString("dietName");
                nutrition = ricejson.getString("classification");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("response",msg + nutrition);

            riceitem = new DietItem(msg,nutrition,idx);
            dietAdapter_rice.addItem(riceitem);
            recyclerViewrice.setAdapter(dietAdapter_rice);
            ricename=riceitem.getName();
            ricelist.add(ricename);

        }
    }

    public static class GetSoup extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String response = null;
            JSONObject jsonObject_soup = new JSONObject();

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos = null;

            try {
                Log.e("진행:","2");
                URL url = new URL(SOUP_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject_soup.put("users_age",MainActivity.user_age);
                    jsonObject_soup.put("expectedDate","2021-09-06T00:00:00.000Z");
                    jsonObject_soup.put("riceDietName",ricename);
                    jsonObject_soup.put("swipeSoup", souplist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("json",jsonObject_soup.toString());
                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject_soup.toString().getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);

                    Log.e("response",response);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            JSONObject result;
            String msg = null, nutrition=null;
            int idx=0;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONObject) responseJSON.get("res_data");
                soupjson = (JSONObject) result.get("soup");
                msg = soupjson.getString("dietName");
                nutrition = soupjson.getString("classification");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            soupitem = new DietItem(msg,nutrition,idx);
            dietAdapter_soup.addItem(soupitem);
            recyclerViewsoup.setAdapter(dietAdapter_soup);
            soupname=soupitem.getName();
            souplist.add(soupname);
        }
    }

    public static class GetSide1 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String response = null;
            JSONObject jsonObject_first = new JSONObject();

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos = null;

            try {
                URL url = new URL(SIDE1_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject_first.put("users_idx",Integer.parseInt(MainActivity.user_idx));
                    jsonObject_first.put("users_age",Integer.parseInt(MainActivity.user_age));
                    jsonObject_first.put("expectedDate",MainActivity.user_expecteddate);
                    jsonObject_first.put("riceDietName",ricename);
                    jsonObject_first.put("soupDietName",soupname);
                    jsonObject_first.put("swipeSide", firstlist);
                    Log.e("json:",jsonObject_first.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject_first.toString().getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);

                    Log.e("response",response);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            JSONArray result;
            String msg = null, nutrition=null;
            int idx=0;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONArray) responseJSON.get("res_data");
                firstresult = result;
                firstlength = result.length();

                firstjson = result.getJSONObject(side1index);
                msg = firstjson.getString("dietName");
                nutrition = firstjson.getString("classification");
                firstitem = new DietItem(msg,nutrition,idx);
                side1list.add(firstitem);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("response",msg + nutrition);

            dietAdapter_first.addItem(firstitem);
            recyclerViewfirst.setAdapter(dietAdapter_first);
            firstname=firstitem.getName();
            firstlist.add(firstname);
        }
    }

    static void Side1set(int index) {

        try {
            String msg = null, nutrition=null;
            firstjson = firstresult.getJSONObject(side1index);
            msg = firstjson.getString("dietName");
            nutrition = firstjson.getString("classification");
            firstitem = new DietItem(msg,nutrition,side1index);
            side1list.add(firstitem);
        } catch (JsonIOException | JSONException e){
            e.printStackTrace();
        }
        dietAdapter_first.addItem(firstitem);
        recyclerViewfirst.setAdapter(dietAdapter_first);
        firstname=firstitem.getName();
        firstlist.add(firstname);

    }

    static void Side2set(int index) {

        try {
            String msg = null, nutrition=null;
            secondjson = secondresult.getJSONObject(index);
            msg = secondjson.getString("dietName");
            nutrition = secondjson.getString("classification");
            seconditem = new DietItem(msg,nutrition,side2index);
            side2list.add(seconditem);
        } catch (JsonIOException | JSONException e){
            e.printStackTrace();
        }
        dietAdapter_second.addItem(seconditem);
        recyclerViewsecond.setAdapter(dietAdapter_second);
        secondname=seconditem.getName();
        secondlist.add(secondname);
    }

    public static class GetSide2 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String response = null;
            JSONObject jsonObject_second = new JSONObject();

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos = null;

            try {
                URL url = new URL(SIDE2_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject_second.put("users_idx",Integer.parseInt(MainActivity.user_idx));
                    jsonObject_second.put("users_age",Integer.parseInt(MainActivity.user_age));
                    jsonObject_second.put("expectedDate","2021-09-06T00:00:00.000Z");
                    jsonObject_second.put("riceDietName",ricename);
                    jsonObject_second.put("soupDietName",soupname);
                    jsonObject_second.put("sideDietName",firstname);
                    jsonObject_second.put("swipeSide", secondlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject_second.toString().getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);
                    Log.e("response",response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            JSONArray result;
            String msg = null, nutrition=null;
            int idx=0;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONArray) responseJSON.get("res_data");
                secondresult = result;
                secondlength = result.length();

                secondjson = result.getJSONObject(side2index);
                msg = secondjson.getString("dietName");
                nutrition = secondjson.getString("classification");
                seconditem = new DietItem(msg,nutrition,idx);
                side2list.add(seconditem);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("response",msg + nutrition);

            dietAdapter_second.addItem(seconditem);
            recyclerViewsecond.setAdapter(dietAdapter_second);
            secondname=seconditem.getName();
            secondlist.add(secondname);

        }
    }

    public class PostDiet extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String response = null;
            JSONObject jsonObject_finsh = new JSONObject();

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos = null;

            try {
                URL url = new URL(FINISH_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject_finsh.put("user_idx",MainActivity.user_idx);
                    jsonObject_finsh.put("diets_idx",finshlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject_finsh.toString().getBytes());
                outputStream.flush();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);
                    Log.e("response",response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Intent intent = new Intent(DietActivity.this,DietinfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ricename",ricename);
            bundle.putString("soupname",soupname);
            bundle.putString("side1name",firstname);
            bundle.putString("side2name",secondname);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.in_left,R.anim.out_right);
            finish();
        }
    }
}