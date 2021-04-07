package com.lck.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class DietinfoActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    String ricename,soupname,side1name,side2name;
    TextView rice,soup,side1,side2;

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


    }
}