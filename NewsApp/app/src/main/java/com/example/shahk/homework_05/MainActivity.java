package com.example.shahk.homework_05;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SourcesHelper {

    ListView listNewsApi;
    ArrayList<Source> sources = new ArrayList<Source>();
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listNewsApi = (ListView)findViewById(R.id.list_news_api);
        setTitle("Main Activity");
        if(sources.size() == 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            builder.setTitle("Loading Sources").setView(inflater.inflate(R.layout.dialog_bar, null));

            dialog = builder.create();
            dialog.show();


        }

        if(isConnected()){
            new GetDataAsync(MainActivity.this).execute("https://newsapi.org/v2/sources?apiKey=6c223359dcca48dcae67cce6d1656135");
        }

        listNewsApi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this,"Item Name:"+sources.get(position).id,Toast.LENGTH_LONG).show();

                Intent i = new Intent(MainActivity.this,NewsActivity.class);
                i.putExtra("sourceObject",sources.get(position));
                startActivity(i);
            }
        });
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
    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;

        setListView();

        dialog.dismiss();



        Toast.makeText(this,"Data Loaded",Toast.LENGTH_SHORT).show();

    }

    public void setListView(){
        ArrayAdapter<Source> adapter =
                new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, sources);

        listNewsApi.setAdapter(adapter);

    }

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Source>> {

        SourcesHelper activity;
        public GetDataAsync(SourcesHelper ah) {
            activity = ah;
        }
        @Override
        protected ArrayList<Source> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray sources = root.getJSONArray("sources");

                    for(int i=0; i<sources.length();i++){

                        JSONObject source_json = sources.getJSONObject(i);
                        Source source = new Source(source_json.getString("id"),source_json.getString("name"));

                        result.add(source);
                    }




                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Source> result) {
            activity.setSources(result);
            if (result.size() > 0) {
                Log.d("demo", result.toString());
            } else {
                Log.d("demo", "empty result");
            }
        }
    }

}





class Source implements Serializable {

    String id,name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}


interface SourcesHelper{
     void setSources(ArrayList<Source> sources);
}

//https://newsapi.org/v2/sources?apiKey=6c223359dcca48dcae67cce6d1656135
