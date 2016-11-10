package com.haha.api;

import android.os.AsyncTask;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchItemsTask().execute();


    }

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+":with" + urlSpec);
            }
            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while ((byteRead = in.read(buffer))>0){
                out.write(buffer, 0, byteRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... params) {
            String s2 ="";
            try {
                byte[] a = getUrlBytes("https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=6afb9770b90660aaaf3ba81410a6762f&format=json&nojsoncallback=1");
                s2 = new String(a, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s2;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }

}
