package com.isispl.customlistviewfromjson;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.isispl.customlistviewfromjson.model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private static final String PHOTO_BASE_URL = "http://services.hanselandpetal.com/photos/";

    ProgressBar pb;
    List<MyBgTask> tasks;
    List<Flower> flowerList;
    Button btn_fetchdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fetchdata = (Button)findViewById(R.id.activity_main_btn_fetchdata);

        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();

        btn_fetchdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                } else {
                    Toast.makeText(MainActivity.this, "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestData(String uri) {
        MyBgTask task = new MyBgTask();
        task.execute(uri); // for parallel request handling, pram1,2,3 can have diff diff urls
        //task.execute("param1", "param2", "param3"); -----> in case of serial execution of task
    }

    public void updateDisplay(){
        //Use of FlowerAdapter to display data
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.row_item, flowerList);
        setListAdapter(adapter);
    }


    //TO check wether Device is connected with internet or not.
    protected boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    private class MyBgTask extends AsyncTask<String, String, List<Flower>>{

        @Override
        protected void onPreExecute() {
            //updateDisplay();
            if (tasks.size() == 0) {                        // to check whether tasks array empty or not.
                // if empty then start progressbar on execute
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Flower> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            flowerList = FlowerJSONParser.parseFeed(content);

            for(Flower flower : flowerList){
                try{
                    String imgURL = PHOTO_BASE_URL + flower.getPhoto();
                    InputStream in = (InputStream)new URL(imgURL).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    flower.setBitmap(bitmap);
                    in.close();
                }
                catch (Exception e){
                    e.printStackTrace();

                }
            }
            return flowerList;

        }

        @Override
        protected void onPostExecute(List<Flower> result) {

           // flowerList = FlowerJSONParser.parseFeed(result);

            updateDisplay();
            tasks.remove(this);             // to remove all task from the tasks array and when it get empty, progress bar should become invisiblee
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }
}