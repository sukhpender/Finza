package com.riggle.plug.utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetFileInfo extends AsyncTask<String, Integer, String> {
    protected String doInBackground(String... urls) {
        URL url;
        String filename = null;
        try {
            url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            conn.setInstanceFollowRedirects(false);
            if (conn.getHeaderField("Content-Disposition") != null) {
                String depo = conn.getHeaderField("Content-Disposition");

                String depoSplit[] = depo.split("filename=");
                filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
            } else {
                filename = "download.pdf";
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
        }
        return filename;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // use result as file name
    }
}
