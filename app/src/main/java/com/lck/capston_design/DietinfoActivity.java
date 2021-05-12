package com.lck.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class DietinfoActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    String ricename,soupname,side1name,side2name;
    TextView rice,soup,side1,side2;
    ImageView ricebtn,soupbtn,firstbtn,secondbtn;
    TextView rice_class,rice_servings,rice_kcal,soup_class,soup_servings,soup_kcal
            ,side1_class,side1_servings,side1_kcal,side2_class,side2_servings,side2_kcal;
    LinearLayout rice_ll, soup_ll, side1_ll, side2_ll;
    int ricestate=0, soupstate=0, side1state=0, side2state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietinfo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ricename=bundle.getString("ricename");
        soupname=bundle.getString("soupname");
        side1name=bundle.getString("side1name");
        side2name=bundle.getString("side2name");

        rice = findViewById(R.id.dietinfo_rice);
        soup = findViewById(R.id.dietinfo_soup);
        side1 = findViewById(R.id.dietinfo_side1);
        side2 = findViewById(R.id.dietinfo_side2);

        ricebtn = findViewById(R.id.dietinfo_btn_rice);
        soupbtn = findViewById(R.id.dietinfo_btn_soup);
        firstbtn = findViewById(R.id.dietinfo_btn_side1);
        secondbtn = findViewById(R.id.dietinfo_btn_side2);

        rice_class = findViewById(R.id.dietinfo_classifi_rice);
        rice_servings = findViewById(R.id.dietinfo_servings_rice);
        rice_kcal = findViewById(R.id.dietinfo_kcal_rice);
        soup_class = findViewById(R.id.dietinfo_classifi_soup);
        soup_servings = findViewById(R.id.dietinfo_servings_soup);
        soup_kcal = findViewById(R.id.dietinfo_kcal_soup);
        side1_class = findViewById(R.id.dietinfo_classifi_side1);
        side1_servings = findViewById(R.id.dietinfo_servings_side1);
        side1_kcal = findViewById(R.id.dietinfo_kcal_side1);
        side2_class = findViewById(R.id.dietinfo_classifi_side2);
        side2_servings = findViewById(R.id.dietinfo_servings_side2);
        side2_kcal = findViewById(R.id.dietinfo_kcal_side2);

        rice_ll = findViewById(R.id.dietinfo_ll_rice);
        soup_ll = findViewById(R.id.dietinfo_ll_soup);
        side1_ll=findViewById(R.id.dietinfo_ll_side1);
        side2_ll = findViewById(R.id.dietinfo_ll_side2);

        rice.setText(ricename);
        soup.setText(soupname);
        side1.setText(side1name);
        side2.setText(side2name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dietinfo_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = (DrawerLayout) findViewById(R.id.dietinfo_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.dietinfo_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id == R.id.tab_eat) {
                    Intent intent = new Intent(DietinfoActivity.this, DietActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.tab_yoga) {
                    Intent intent = new Intent(DietinfoActivity.this, YogaActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.tab_hos) {
                    Intent intent = new Intent(DietinfoActivity.this, HospitalActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.tab_logout){
                    Intent intent = new Intent(DietinfoActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.tab_set){
                    Intent intent = new Intent(DietinfoActivity.this,SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id==R.id.tab_findhos){
                    Intent intent = new Intent(DietinfoActivity.this,FindhospitalActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        ricebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ricestate==0){
                    rice_ll.setVisibility(View.VISIBLE);
                    ricestate=1;
                } else {
                    rice_ll.setVisibility(View.GONE);
                    ricestate=0;
                }
            }
        });
        soupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soupstate==0){
                    soup_ll.setVisibility(View.VISIBLE);
                    soupstate=1;
                } else {
                    soup_ll.setVisibility(View.GONE);
                    soupstate=0;
                }
            }
        });
        firstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (side1state==0){
                    side1_ll.setVisibility(View.VISIBLE);
                    side1state=1;
                } else {
                    side1_ll.setVisibility(View.GONE);
                    side1state=0;
                }
            }
        });
        secondbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (side2state==0){
                    side2_ll.setVisibility(View.VISIBLE);
                    side2state=1;
                } else {
                    side2_ll.setVisibility(View.GONE);
                    side2state=0;
                }
            }
        });
        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ricestate==0){
                    rice_ll.setVisibility(View.VISIBLE);
                    ricestate=1;
                } else {
                    rice_ll.setVisibility(View.GONE);
                    ricestate=0;
                }
            }
        });
        soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soupstate==0){
                    soup_ll.setVisibility(View.VISIBLE);
                    soupstate=1;
                } else {
                    soup_ll.setVisibility(View.GONE);
                    soupstate=0;
                }
            }
        });
        side1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (side1state==0){
                    side1_ll.setVisibility(View.VISIBLE);
                    side1state=1;
                } else {
                    side1_ll.setVisibility(View.GONE);
                    side1state=0;
                }
            }
        });
        side2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (side2state==0){
                    side2_ll.setVisibility(View.VISIBLE);
                    side2state=1;
                } else {
                    side2_ll.setVisibility(View.GONE);
                    side2state=0;
                }
            }
        });
        JSONObject ricejson = DietActivity.ricejson;
        JSONObject soupjson = DietActivity.soupjson;
        JSONObject firstjson = DietActivity.firstjson;
        JSONObject secondjson = DietActivity.secondjson;

        try {
            rice_class.setText(ricejson.getString("classification"));
            rice_servings.setText(ricejson.getString("serving"));
            rice_kcal.setText(ricejson.getString("kcal")+" kcal");
            soup_class.setText(soupjson.getString("classification"));
            soup_servings.setText(soupjson.getString("serving"));
            soup_kcal.setText(soupjson.getString("kcal")+" kcal");
            side1_class.setText(firstjson.getString("classification"));
            side1_servings.setText(firstjson.getString("serving"));
            side1_kcal.setText(firstjson.getString("kcal")+" kcal");
            side2_class.setText(secondjson.getString("classification"));
            side2_servings.setText(secondjson.getString("serving"));
            side2_kcal.setText(secondjson.getString("kcal")+" kcal");
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
}