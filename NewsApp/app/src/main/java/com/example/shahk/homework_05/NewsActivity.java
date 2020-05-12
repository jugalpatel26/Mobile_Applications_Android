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
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements ArticleHelper{

    ArrayList<NewsArticle> articles = new ArrayList<NewsArticle>();
    AlertDialog dialog;
    ListView listNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitle("News Activity");
        listNews = (ListView)findViewById(R.id.list_news);

        if(articles.size() ==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            builder.setTitle("Loading Sources").setView(inflater.inflate(R.layout.dialog_bar, null));

            dialog = builder.create();
            dialog.show();
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            Source source = (Source) getIntent().getExtras().getSerializable("sourceObject");

//            Toast.makeText(NewsActivity.this, source.id + " " + source.name, Toast.LENGTH_LONG).show();

            if(isConnected()){
                new GetArticlesAsync(NewsActivity.this).execute("https://newsapi.org/v2/top-headlines?sources="+source.id+"&apiKey=6c223359dcca48dcae67cce6d1656135");
            }




        }
        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(NewsActivity.this,webActivity.class);
                i.putExtra("weburl",articles.get(position).url);
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
    public void setArticles(ArrayList<NewsArticle> result) {
        this.articles = result;

        NewsArticleAdapter adapter = new NewsArticleAdapter(this,R.layout.news_item,this.articles);
        listNews.setAdapter(adapter);
        dialog.dismiss();
//        Toast.makeText(NewsActivity.this,"Data Loaded",Toast.LENGTH_LONG).show();


    }

    private class GetArticlesAsync extends AsyncTask<String, Void, ArrayList<NewsArticle>> {

        ArticleHelper activity;

        public GetArticlesAsync(ArticleHelper activity) {
            this.activity = activity;
        }

        @Override
        protected ArrayList<NewsArticle> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<NewsArticle> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray articlesJson = root.getJSONArray("articles");
                    for(int i=0;i<articlesJson.length();i++){
                        JSONObject articleJson = articlesJson.getJSONObject(i);
                        Log.d("demo","Article: "+articleJson);

                        NewsArticle article = new NewsArticle();
                        article.author = articleJson.getString("author");
                        article.publishedAt = articleJson.getString("publishedAt");
                        article.title = articleJson.getString("title");
                        article.urlToImage = articleJson.getString("urlToImage");
                        article.url = articleJson.getString("url");

                        result.add(article);

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
        protected void onPostExecute(ArrayList<NewsArticle> result) {

            activity.setArticles(result);


            if (result.size() > 0) {
                Log.d("Kush", result.toString());
            } else {
                Log.d("Kush", "empty result");
            }
        }
    }
}
//https://newsapi.org/v2/top-headlines?sources=<Source_id>&apiKey=6c223359dcca48dcae67cce6d1656135
class NewsArticle{
    String title,author,publishedAt,urlToImage,url;

    public NewsArticle() {
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }
}

interface ArticleHelper{
     void setArticles(ArrayList<NewsArticle> result);
}

