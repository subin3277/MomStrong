package com.example.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class YogaActivity extends AppCompatActivity {

    TextView firstbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        firstbtn=findViewById(R.id.yoga_btn_1st);

        firstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YogaActivity.this,Yoga1stActivity.class);
                startActivity(intent);
            }
        });
    }
}