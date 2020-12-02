package com.example.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DietinfoActivity extends AppCompatActivity {

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

        rice.setText(ricename);
        soup.setText(soupname);
        side1.setText(side1name);
        side2.setText(side2name);


    }
}