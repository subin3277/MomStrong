package com.example.capston_design;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tensorflow.lite.examples.posenet.lib.KeyPoint;
import org.tensorflow.lite.examples.posenet.lib.Person;
import org.tensorflow.lite.examples.posenet.lib.Posenet;
import org.tensorflow.lite.examples.posenet.lib.Position;

public class YogacamActivity extends AppCompatActivity {

    PosenetFragment posenetFragment;
    ImageView imagepreview;
    private ArrayList<YogaItem> firstlist =new ArrayList<>();
    private String YOGA1st_URL = "http://13.125.245.6:3000/api/yogas/getYogas?trimester=1st";
    Paint paint = new Paint();
    SurfaceHolder surfaceHolder;
    Bitmap bitmap;
    Posenet posenet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yogacam);

        posenetFragment = new PosenetFragment();
        imagepreview = findViewById(R.id.imagepreview);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, posenetFragment).commit();

        Person person = posenet.estimateSinglePose(bitmap);
        Canvas canvas = surfaceHolder.lockCanvas();
        Draw(canvas,person,bitmap);

    }

    public void setPaint(){
        paint.setColor(Color.RED);
        paint.setTextSize(80.0f);
        paint.setStrokeWidth(8.0f);
    }


    public void Draw(Canvas canvas,Person person,Bitmap bitmap){
        int screenWidth,screenHeigth,left,right,top,bottom;

        Point point = new Point();
        this.getWindowManager().getDefaultDisplay().getRealSize(point);
        int width = point.x;
        int height = (int) ((point.y)*0.3);

        setPaint();
        canvas.drawBitmap(
                bitmap,
                new Rect(0,0,width,height),
                new Rect(0, 0, 0, height),
                paint);

        float widthRatio = (float) width / (float)257.00;
        float heightRatio = (float) point.y / (float)257.00;

        List<KeyPoint> var15 = person.getKeyPoints();
        KeyPoint keyPoint = null;

        
        for (var15.contains(keyPoint)){
            if ((double)keyPoint.getScore() > 0.5) {
                Position position = keyPoint.getPosition();
                float adjustedX = (float)position.getX() * widthRatio;
                float adjustedY = (float)position.getY() * heightRatio;
                canvas.drawCircle(adjustedX, adjustedY, 8.0f, this.paint);
            }
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    private class GetYoga1st extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(YOGA1st_URL);

            Log.e("json",jsonStr);

            if (jsonStr!= null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray array = jsonObject.getJSONArray("res_data");

                    for (int i=0; i<array.length();i++){
                        JSONObject c =array.getJSONObject(i);

                        String pose = c.getString("pose");
                        String path = c.getString("path");

                        firstlist.add(new YogaItem(pose,path));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            bitmap=getImageFromURL(firstlist.get(0).path);
        }
    }

    public static Bitmap getImageFromURL(String imageURL){
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;

        try
        {
            URL url = new URL(imageURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            e.printStackTrace();
        } finally{
            if(bis != null) {
                try {bis.close();} catch (IOException e) {}
            }
            if(conn != null ) {
                conn.disconnect();
            }
        }

        return imgBitmap;
    }

}