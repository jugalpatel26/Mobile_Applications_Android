package com.example.shahk.homework_05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webActivity extends AppCompatActivity {
    String weburl;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webactivity);
        weburl = getIntent().getExtras().getString("weburl");
        Log.d("url",weburl);
        webView=(WebView) findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(weburl);


    }
}
