package com.example.jugal.hw04_http;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements downloadImage.PutImage{
    public String[] keywords;
    public String[] urls=null;
    TextView keyword;
    TextView loading;
    ImageView imageView;
    public ProgressBar pb;
    Button go;
    ImageButton previous;
    ImageButton next;
    String selectedKeyword="";
    int c=0;
    int l;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        go = (Button) findViewById(R.id.button_go);
        keyword = (TextView) findViewById(R.id.textView);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        loading = (TextView) findViewById(R.id.Loading);
        loading.setVisibility(View.INVISIBLE);
        previous = (ImageButton) findViewById(R.id.imageButton_previous);
        next = (ImageButton) findViewById(R.id.imageButton_next);
        previous.setEnabled(false);
        next.setEnabled(false);

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose Keyword")
                            .setItems(keywords, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectedKeyword = keywords[which];
                                    keyword.setText(selectedKeyword);
                                    if(isConnected()) {
                                        new GetDataAsyncUrls().execute("http://dev.theappsdr.com/apis/photos/index.php?keyword=" + selectedKeyword);
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this," there is no internet connection and do not attempt to send the HTTP request.",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                    AlertDialog keys = builder.create();
                    keys.show();
                }
            });
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()) {
                        c--;
                        if (c < 0) {
                            c = l - 1;
                            new downloadImage(MainActivity.this).execute(urls[c]);
                        } else {
                            new downloadImage(MainActivity.this).execute(urls[c]);
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this," there is no internet connection and do not attempt to send the HTTP request.",Toast.LENGTH_LONG).show();
                    }
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()) {
                        c++;
                        if (c > l - 1) {
                            c = 0;
                            new downloadImage(MainActivity.this).execute(urls[c]);
                        } else {
                            new downloadImage(MainActivity.this).execute(urls[c]);
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this," there is no internet connection and do not attempt to send the HTTP request.",Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isConnected()){
            new GetDataAsyncKeyword().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
        }
        else {
            Toast.makeText(MainActivity.this," there is no internet connection",Toast.LENGTH_LONG).show();
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void putImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        l = urls.length;
        if(l>1)
        {
            previous.setEnabled(true);
            next.setEnabled(true);
        }
        else {

        }

    }

    @Override
    public void progressVisible() {

//        pb.setVisibility(View.VISIBLE);
//        loading.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setTitle("Loading Dictionary").setView(inflater.inflate(R.layout.dialog_bar, null));

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void progressInvisible() {
        dialog.dismiss();
//        pb.setVisibility(View.INVISIBLE);
//        loading.setVisibility(View.INVISIBLE);
    }


    @Override
    public void disableButton() {
        next.setEnabled(false);
        previous.setEnabled(false);

    }


    private class GetDataAsyncKeyword extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    keywords = result.split(";");
                }
                return keywords;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                Log.d("demo", result[1]);
            } else {
                Log.d("demo", "null result");
            }
        }
    }
    private class GetDataAsyncUrls extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            Log.d("b",params[0]);
            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    if(result!=null) {
                        urls = result.split("\\s+");
                    }
                }
                return urls;
            } catch (MalformedURLException e) {
                e.printStackTrace(); }
                catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Log.d("Kush",result.length+"");
            Log.d("Kush","Result is:"+result[0]);
            if (result != null) {
                if(result[0].length() > 0){
                    previous.setEnabled(false);
                    next.setEnabled(false);
                    if(isConnected()) {
                        new downloadImage(MainActivity.this).execute(result[0]);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this," there is no internet connection and do not attempt to send the HTTP request.",Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(MainActivity.this,"No Images Found",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MainActivity.this,"No Images Found",Toast.LENGTH_LONG).show();
            }
        }


        }
    }








