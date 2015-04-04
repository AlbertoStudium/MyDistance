package com.example.android.mydistance.app;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alberto on 23/03/2015.
 */
public class BuildUriTask {



    private String origen;
    private String destino;
    private String language="es-ES";
    private String units;
    private String mode;
    private String uriString;
    private String sensor ="false";
    URL url = null;

    private static final String DESTINATION = "destinations";
    private static final String LANGUAGE = "language";
    private static final String SENSOR= "sensor";
    private static final String BASEURL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final String ORIGIN = "origins";
    private static final String MODE = "mode";



    public BuildUriTask(String destino, String origen, String mode) {
        this.destino = destino;
        this.origen = origen;
        this.mode = mode;


    }

   public URL  GetUrl() {


       Uri builtUri = Uri.parse(BASEURL).buildUpon()
               .appendQueryParameter(ORIGIN, this.origen)
               .appendQueryParameter(DESTINATION, this.destino)
               .appendQueryParameter(MODE, this.mode)
               .appendQueryParameter(LANGUAGE, this.language)
               .appendQueryParameter(SENSOR, this.sensor)
               .build();

       try {

           url = new URL(builtUri.toString());


       } catch (MalformedURLException e) {
           e.printStackTrace();
       }

       return this.url;
   }
}
