package com.lck.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    Button next;
    EditText id,password,password2;
    String txtid,txtpw,txtpw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        next=findViewById(R.id.signup_btn);
        id = findViewById(R.id.signup_et_id);
        password = findViewById(R.id.signup_et_password);
        password2 = findViewById(R.id.signup_et_password2);

        SharedPreferences auto = getSharedPreferences("signup", Activity.MODE_PRIVATE);
        txtid =auto.getString("id",null);
        txtpw = auto.getString("pw",null);
        txtpw2 = auto.getString("pw2",null);

        if(txtid!=null || txtpw!=null || txtpw2!=null){
            id.setText(txtid);
            password.setText(txtpw);
            password2.setText(txtpw2);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().toString().equals("")){
                    Toast.makeText(SignupActivity.this,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if (password.getText().toString().equals("")){
                    Toast.makeText(SignupActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if (password2.getText().toString().equals("")){
                    Toast.makeText(SignupActivity.this,"비밀번호 확인을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences.Editor signup = auto.edit();
                    signup.putString("id",id.getText().toString());
                    signup.putString("pw",password.getText().toString());
                    signup.putString("pw2",password2.getText().toString());
                    signup.commit();

                    Bundle bundle = new Bundle();
                    bundle.putString("id",id.getText().toString());
                    bundle.putString("password",password.getText().toString());
                    bundle.putString("password2",password2.getText().toString());

                    Intent intent = new Intent(SignupActivity.this, Signup2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_left,R.anim.out_right);
                    finish();
                }

            }
        });
    }
}