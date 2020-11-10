package com.example.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    InfoFragment infoFragment;
    DietFragment dietFragment;
    YogaFragment yogaFragment;
    HospFragment hospFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottomNavigationView = findViewById(R.id.bottomTab);
        infoFragment = new InfoFragment();
        dietFragment = new DietFragment();
        yogaFragment = new YogaFragment();
        hospFragment = new HospFragment();

        //getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,infoFragment).commit();
/*
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,infoFragment).commit();
                        return true;
                    case R.id.tab_eat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,dietFragment).commit();
                        return true;
                    case R.id.tab_yoga:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,yogaFragment).commit();
                        return true;
                    case R.id.tab_hos:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,hospFragment).commit();
                        return true;
                }
                return false;
            }
        });*/
    }
}