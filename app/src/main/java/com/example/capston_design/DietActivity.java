package com.example.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

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
    static String ricename=null,soupname=null,firstname=null,secondname=null;
    Button finish1,finish2,finish3,finish4;
    TextView soup,first,second,tvnull;
    static int users_age=22;

    static ArrayList<String> ricelist = new ArrayList<>();
    static ArrayList<String> souplist = new ArrayList<>();
    static ArrayList<String> firstlist = new ArrayList<>();
    static ArrayList<String> secondlist = new ArrayList<>();

    static String RICE_URL = "http://13.125.245.6:3000/api/diets/postRiceDiets";
    static String SOUP_URL = "http://13.125.245.6:3000/api/diets/postSoupDiets";
    static String SIDE1_URL= "http://13.125.245.6:3000/api/diets/postSideDiets1";
    static String SIDE2_URL= "http://13.125.245.6:3000/api/diets/postSideDiets2";

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

        drawerLayout = (DrawerLayout) findViewById(R.id.diet_drawer_layout);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.diet_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id == R.id.tab_info) {
                    Intent intent = new Intent(DietActivity.this, InformationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.tab_eat) {
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

                LinearLayoutManager manager = new LinearLayoutManager(DietActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewsoup.setLayoutManager(manager);

                dietAdapter_soup = new DietAdapter();
                recyclerViewsoup.setAdapter(dietAdapter_soup);

                helper = new ItemTouchHelper(new ItemTouchHelperCallback_soup(dietAdapter_soup));
                helper.attachToRecyclerView(recyclerViewsoup);

                new GetSoup().execute();
            }
        });


        finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first.setVisibility(View.VISIBLE);
                recyclerViewfirst.setVisibility(View.VISIBLE);
                finish3.setVisibility(View.VISIBLE);

                LinearLayoutManager manager = new LinearLayoutManager(DietActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewfirst.setLayoutManager(manager);

                dietAdapter_first = new DietAdapter();
                recyclerViewfirst.setAdapter(dietAdapter_first);

                helper = new ItemTouchHelper(new ItemTouchHelperCallback_first(dietAdapter_first));
                helper.attachToRecyclerView(recyclerViewfirst);

                new GetSide1().execute();

            }
        });

        finish3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                second.setVisibility(View.VISIBLE);
                recyclerViewsecond.setVisibility(View.VISIBLE);
                finish4.setVisibility(View.VISIBLE);

                LinearLayoutManager manager = new LinearLayoutManager(DietActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewsecond.setLayoutManager(manager);

                dietAdapter_second = new DietAdapter();
                recyclerViewsecond.setAdapter(dietAdapter_second);

                helper = new ItemTouchHelper(new ItemTouchHelperCallback_second(dietAdapter_second));
                helper.attachToRecyclerView(recyclerViewsecond);

                new GetSide2().execute();

            }
        });

        finish4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DietActivity.this,DietinfoActivity.class);
                startActivity(intent);
                finish();
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
                Log.e("진행:","1");
                URL url = new URL(RICE_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try {
                    jsonObject_rice.put("users_age",users_age);
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

            JSONObject result, rice;
            String msg = null , nutrition = null;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONObject) responseJSON.get("res_data");
                rice = (JSONObject) result.get("Rice");
                msg = rice.getString("dietName");
                nutrition = rice.getString("classification");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("response",msg + nutrition);

            riceitem = new DietItem(msg,nutrition);
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
                    jsonObject_soup.put("users_age",users_age);
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

            JSONObject result, rice;
            String msg = null , nutrition = null;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONObject) responseJSON.get("res_data");
                rice = (JSONObject) result.get("soup");
                msg = rice.getString("dietName");
                nutrition = rice.getString("classification");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            soupitem = new DietItem(msg,nutrition);
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
                    jsonObject_first.put("users_age",users_age);
                    jsonObject_first.put("riceDietName",ricename);
                    jsonObject_first.put("soupDietName",soupname);
                    jsonObject_first.put("swipeSide", firstlist);
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

            JSONObject result, rice;
            String msg = null , nutrition = null;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONObject) responseJSON.get("res_data");
                rice = (JSONObject) result.get("side");
                msg = rice.getString("dietName");
                nutrition = rice.getString("classification");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("response",msg + nutrition);

            firstitem = new DietItem(msg,nutrition);
            dietAdapter_first.addItem(firstitem);
            recyclerViewfirst.setAdapter(dietAdapter_first);
            firstname=firstitem.getName();
            firstlist.add(firstname);
        }
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
                    jsonObject_second.put("users_age",users_age);
                    jsonObject_second.put("riceDietName",ricename);
                    jsonObject_second.put("soupDietName",soupname);
                    jsonObject_second.put("sideDietName",firstname);
                    jsonObject_second.put("swipeRice", secondlist);
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

            JSONObject result, rice;
            String msg = null , nutrition = null;

            try {
                JSONObject responseJSON = new JSONObject(response);
                result = (JSONObject) responseJSON.get("res_data");
                rice = (JSONObject) result.get("side");
                msg = rice.getString("dietName");
                nutrition = rice.getString("classification");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("response",msg + nutrition);

            seconditem = new DietItem(msg,nutrition);
            dietAdapter_second.addItem(seconditem);
            recyclerViewsecond.setAdapter(dietAdapter_second);
            secondname=seconditem.getName();
            secondlist.add(secondname);
        }
    }
}