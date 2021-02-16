package com.example.capston_design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.posenet.lib.Posenet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText id,password;
    Button signup, go, login;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String SIGNIN_URL="http://13.125.245.6:3000/api/users/signin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        saveToken();

        signup=findViewById(R.id.login_btn_signup);
        go = findViewById(R.id.login_btn_go);
        id = findViewById(R.id.login_et_id);
        password = findViewById(R.id.login_et_password);
        login=findViewById(R.id.login_btn_login);


        Button test = findViewById(R.id.login_btn_test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtid = id.getText().toString();
                String txtpw = password.getText().toString();

                if(txtid.equals("")||txtpw.equals("")){
                    Toast.makeText(LoginActivity.this,"아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else {

                    login(txtid,txtpw);
                }
            }
        });


    }

    public void saveToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()){
                    return;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("UserTokenKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String token = task.getResult().getToken();
                editor.putString("TokenCode",token);
                editor.apply();
            }
        });
    } //sharedpreferences에 토큰값저장해놓기.

    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserTokenKey", MODE_PRIVATE);
        return sharedPreferences.getString("TokenCode", "");
    }  //sharedpreference에서 값꺼내기

    public void login(String id,String pw){

        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos=null;

        try {
            URL URL = new URL(SIGNIN_URL);
            connection=(HttpURLConnection) URL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cache-Control","no-cache");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("id",id);
                jsonObject.put("pw",pw);
                jsonObject.put("token",getToken());

            } catch (JSONException e){
                e.printStackTrace();
            }
            Log.e("json","생성한 json"+jsonObject.toString());

            outputStream = connection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();

            String response;
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray(); response = new String(byteData);

                response = new String(byteData);

                try {
                    JSONObject responseJSON = new JSONObject(response);
                    String result = (String) responseJSON.get("res_state");
                    String msg = (String) responseJSON.get("res_msg");


                    switch (result) {
                        case "invalid_data":
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                        case "success":

                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                    }


                } catch (JSONException e){
                    e.printStackTrace();
                }

            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}