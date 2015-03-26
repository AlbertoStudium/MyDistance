package com.example.android.mydistance.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by alberto on 20/03/2015.
 */
public class DistanceTask extends AsyncTask<String,Void,Void>{


    private static final String LOG_TAG = "LOG";
    private String ResumenDistancia;


    @Override
    protected Void doInBackground(String... params) {

        String Origen = params[0];
        String Destino =params[1];



        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            BuildUriTask peticionUrl = new BuildUriTask(Destino,Origen);
            URL url = peticionUrl.GetUrl();


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            ResumenDistancia = buffer.toString();

            ParseJsonTask json = new ParseJsonTask(ResumenDistancia);

            json.ReadJson();
          //  Log.e(LOG_TAG,ResumenDistancia);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }


        return null;
    }
}