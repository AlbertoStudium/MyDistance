package com.example.android.mydistance.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.android.mydistance.app.Data.DistanceProvider;

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
public class DistanceTask extends AsyncTask<String,Void,Uri>{


    private static final String LOG_TAG = "LOG";
    private String ResumenDistancia;
    private String status;
    private String status2;
    private Context context;
    private Escuchador escuchador;

    public DistanceTask(Context context, Escuchador escuchador) {
        this.context = context;
        this.escuchador = escuchador;
    }


    @Override
    protected Uri doInBackground(String... params) {

        String Origen = params[0];
        String Destino =params[1];
        String Mode =params[2];



        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            BuildUriTask peticionUrl = new BuildUriTask(Destino,Origen,Mode);
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
            status = json.getStatus();
            status2 = json.getStatus2();


            if (status.equals("OK")&& status2.equals("OK")){

                ContentValues values = new ContentValues();

                values.put(DistanceProvider.Distance.COL_ORIGEN, json.getOrigin());
                values.put(DistanceProvider.Distance.COL_DESTINO, json.getDestination());
                values.put(DistanceProvider.Distance.COL_DISTANCIA, json.getDistance());
                values.put(DistanceProvider.Distance.COL_DURACION, json.getDuration());


                ContentResolver cr = context.getContentResolver();

                Uri newUriElement = cr.insert(DistanceProvider.CONTENT_URI, values);
                return newUriElement;
            }else{

                return null;
            }


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

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        escuchador.heTerminado(uri);
    }
}
