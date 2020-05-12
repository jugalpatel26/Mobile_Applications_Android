package com.example.jugal.inclass4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    String threadImgUrl;
    String asyncImgUrl;
    Button imageFromThread;
    Button imageFromAsync;
    ImageView imgView;
//    ProgressBar progress;
    ExecutorService executorService;
    ProgressBar progressBarCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViews();

        progressBarCircle.setVisibility(View.INVISIBLE);

        threadImgUrl = "https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";
        asyncImgUrl = "https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";



        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if(msg.getData().getParcelable("ImageFromThread") != null) {
                    Bitmap myBitMap = msg.getData().getParcelable("ImageFromThread");

                    imgView.setImageBitmap(myBitMap);

//                    progress.setProgress(0);
                    return true;

                }
                else{

                    if(msg.getData().getInt("Progress") == 99){
                        progressBarCircle.setVisibility(View.INVISIBLE);
                    }

//                    progress.setProgress(msg.getData().getInt("Progress"));

                }

                return false;
            }
        });

        executorService = Executors.newFixedThreadPool(2);

        imageFromThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBarCircle.setVisibility(View.VISIBLE);

                executorService.execute(new DoThreadTask(threadImgUrl));


//                Thread thread = new Thread(new DoThreadTask(threadImgUrl));
//                thread.start();
//                progress.setMax(100);

            }

        });

        imageFromAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DoAsyncTask().execute(asyncImgUrl);

//                progress.setProgress(0);
            }
        });
    }

    public class DoThreadTask implements Runnable {

        String imgUrl;
        DoThreadTask(String url){

            this.imgUrl = url;

        }



        @Override
        public void run() {

            Bitmap myBitMap = getImageBitmap(this.imgUrl);


            for(int i=0; i<100 ; i++){

                for(int j=0; j<1000000; j++){


                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("Progress",i);
                message.setData(bundle);
                handler.sendMessage(message);


            }


            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("ImageFromThread",myBitMap);
            message.setData(bundle);
            handler.sendMessage(message);







        }

        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    void findAllViews(){
        imageFromThread = (Button)findViewById(R.id.button_thread);
        imgView = (ImageView)findViewById(R.id.imageView);
//        progress = (ProgressBar)findViewById(R.id.progressBar);
        imageFromAsync = (Button)findViewById(R.id.button_async);
        progressBarCircle = (ProgressBar)findViewById(R.id.progressBar2);
    }

    class DoAsyncTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected void onPreExecute() {
//            progress.setProgress(0);
            progressBarCircle.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
//            progress.setProgress(0);
            progressBarCircle.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

//            progress.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myBitMap = getImageBitmap(strings[0]);

            for(int i=0; i<100;i++){
                for(int j=0; j<100000; j++){

                }
                publishProgress(i);
            }
            return myBitMap;
        }
        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}


