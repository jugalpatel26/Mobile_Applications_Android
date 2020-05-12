package com.example.jugal.hw04_http;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class downloadImage extends AsyncTask<String,Void,Bitmap> {

    PutImage putImage;
    public downloadImage(PutImage putImage) {
        this.putImage = putImage;
    }

    @Override
    protected void onPreExecute() {
        putImage.progressVisible();

    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection = null;
        Bitmap image = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }
                return image;
            } catch (MalformedURLException e) {
                putImage.disableButton();
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
    protected void onPostExecute(Bitmap bitmap) {
          putImage.putImage(bitmap);
          putImage.progressInvisible();

    }
    public static interface PutImage
    {
        public void putImage(Bitmap bitmap);
        public void progressVisible();
        public void progressInvisible();
        public void disableButton();

    }

}
