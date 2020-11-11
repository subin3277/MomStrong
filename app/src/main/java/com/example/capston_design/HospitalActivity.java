package com.example.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class HospitalActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        Toolbar toolbar = (Toolbar) findViewById(R.id.hosp_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id==R.id.tab_info){
                    Intent intent = new Intent(HospitalActivity.this, InformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id==R.id.tab_eat){
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