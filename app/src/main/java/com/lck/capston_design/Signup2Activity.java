package com.lck.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Signup2Activity extends AppCompatActivity {

    TextView expecteddate;
    EditText name,age,weight;
    String id,password,password2,txttime;
    DatePickerDialog datePickerDialog;
    ReturnDate returnDate;
    String year,month,day;
    int y,m,d;
    Button signupbtn,backbtn;
    String txtname,txtage,txtweight,txtdate;
    int numage,numweigt;
    String spname,spage,spdate;
    static int users_idx;

    String SIGNUP_URL = "http://13.125.245.6:3000/api/users/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        name = findViewById(R.id.signup2_et_name);
        age = findViewById(R.id.signup2_et_age);
        //weight = findViewById(R.id.signup2_et_weight);
        signupbtn = findViewById(R.id.signup2_btn);
        backbtn=findViewById(R.id.signup2_backbtn);

        expecteddate = findViewById(R.id.signup2_et_date);
        returnDate = new ReturnDate();

        year = returnDate.returnYear();
        month = returnDate.returnMonth();
        day = returnDate.returnDay();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        id = bundle.getString("id");
        password = bundle.getString("password");
        password2 = bundle.getString("password2");

        SharedPreferences auto = getSharedPreferences("signup", Activity.MODE_PRIVATE);
        spname =auto.getString("name",null);
        spage = auto.getString("age",null);
        spdate = auto.getString("date",null);

        if(spname!=null || spage!=null || spdate!=null){
            name.setText(spname);
            age.setText(spage);
            expecteddate.setText(spdate);
        }

        expecteddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(Signup2Activity.this, listener, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePickerDialog.show();
            }

        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor signup = auto.edit();
                signup.putString("name",name.getText().toString());
                signup.putString("age",age.getText().toString());
                signup.putString("date",expecteddate.getText().toString());
                signup.commit();

                Intent intent1 = new Intent(Signup2Activity.this,SignupActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_right,R.anim.out_left);
                finish();
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtname = name.getText().toString();
                txtage = age.getText().toString();
                txtdate = expecteddate.getText().toString();

                if ((txtname.equals(""))||(txtage.equals(""))||(txtdate.equals(""))){
                    Toast.makeText(Signup2Activity.this,"모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {

                    new signup().execute();

                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            y = i;
            m = i1 + 1;
            d = i2;
            year = String.valueOf(y);
            month = String.valueOf(m);
            day = String.valueOf(d);
            if (m<10){
                month="0"+month;
            }
            if (d<10){
                day = "0"+day;
            }



            expecteddate.setText(year+"-"+month+"-"+day);
            Log.e("date",year+" - "+month+" - "+day);
        }
    };

    public class ReturnDate{
        long now;
        Date date;
        SimpleDateFormat sdf;
        ReturnDate(){
            now=System.currentTimeMillis();
            date=new Date(now);
        }
        public String returnYear(){
            sdf = new SimpleDateFormat("yyyy");
            String year = sdf.format(date);
            return year;
        }
        public String returnMonth(){
            sdf = new SimpleDateFormat("MM");
            String month = sdf.format(date);
            return month;
        }
        public String returnDay(){
            sdf = new SimpleDateFormat("dd");
            String day = sdf.format(date);
            return day;
        }
    }

    private class signup extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            ByteArrayOutputStream baos=null;
            String response = null;

            try {
                URL URL = new URL(SIGNUP_URL);
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
                    jsonObject.put("pw",password);
                    jsonObject.put("pw2",password2);
                    jsonObject.put("userName",txtname);
                    jsonObject.put("age",txtage);
                    //jsonObject.put("weight",txtweight);
                    jsonObject.put("expectedDate",txtdate+"T00:00:00.000Z");

                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.e("json","생성한 json"+jsonObject.toString());

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                outputStream.flush();

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

                    Log.e("response",response);

                }

            } catch (IOException e){
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject responseJSON = new JSONObject(s);
                String result = (String) responseJSON.get("res_state");
                String msg = (String) responseJSON.get("res_msg");

                switch (result) {
                    case "id_is_invalid_form":
                        Toast.makeText(Signup2Activity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                    case "pw_is_invalid_form":
                        Toast.makeText(Signup2Activity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                    case "userName_is_invalid_form":
                        Toast.makeText(Signup2Activity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                    case "pw_do_not_match":
                        Toast.makeText(Signup2Activity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                    case "email_is_already_used":
                        Toast.makeText(Signup2Activity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                    case "success":
                        Toast.makeText(Signup2Activity.this, msg, Toast.LENGTH_SHORT).show();
                        users_idx = responseJSON.getInt("res_data");
                        SharedPreferences auto = getSharedPreferences("signup", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = auto.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(Signup2Activity.this, Signup3Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_left,R.anim.out_right);
                        finish();
                        break;
                    default:
                        Toast.makeText(Signup2Activity.this, "회원가입에 실패했습니다..", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
}