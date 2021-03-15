package com.example.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context =this;

    LinearLayout diet,yoga,hosp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diet=findViewById(R.id.main_menu_diet);
        yoga=findViewById(R.id.main_menu_yoga);
        hosp=findViewById(R.id.main_menu_hos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id==R.id.tab_info){
                    Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id==R.id.tab_eat){
                    Intent intent = new Intent(MainActivity.this, DietActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id==R.id.tab_yoga){
                    Intent intent = new Intent(MainActivity.this, YogaActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (id==R.id.tab_hos){
                    Intent intent = new Intent(MainActivity.this, HospitalActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DietActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_left,R.anim.out_right);
                finish();
            }
        });
        yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, YogaActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_left,R.anim.out_right);
                finish();
            }
        });
        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HospitalActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_left,R.anim.out_right);
                finish();
            }
        });

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

}