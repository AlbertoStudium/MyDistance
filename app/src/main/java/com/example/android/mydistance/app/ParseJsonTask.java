package com.example.android.mydistance.app;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alberto on 24/03/2015.
 */
public class ParseJsonTask {

    private static final String STATUS = "status";
    private static final String LOG_TAG = "LOG";
    private static final String ORIGIN = "origin_addresses";
    private static final String DESTINATION = "destination_addresses";
    private static final String ELEMENT = "elements";
    private static final String ROWS = "rows";
    private static final String DISTANCE = "text";
    private static final String DURATION = "text";
    private String json;



    private String distance;
    private String duration;
    private String origin;
    private String destination;
    private String status;


    public ParseJsonTask(String json) {
        this.json = json;
    }

 public void ReadJson(){

     try {

         // recogemos el primer objeto json y desglosamos sus arrays
         JSONObject JsonRequest = new JSONObject(this.json);
         this.status= JsonRequest.getString(STATUS);
         JSONArray OriginArray = JsonRequest.getJSONArray(ORIGIN);
         JSONArray DestinationArray = JsonRequest.getJSONArray(DESTINATION);
         JSONArray RowsArray = JsonRequest.getJSONArray(ROWS);

         this.origin = OriginArray.get(0).toString();
         this.destination = DestinationArray.get(0).toString();

        //recogemos el segundo objeto json

         JSONObject JsonRows = new JSONObject(RowsArray.get(0).toString());
         JSONArray elements = JsonRows.getJSONArray(ELEMENT);

         //recogemos el tercer objeto json

         JSONObject firstRowElement = new JSONObject(elements.get(0).toString());

         // recogemos el cuarto y quinto 0bjeto json

         JSONObject distanceElement = firstRowElement.getJSONObject("distance");
         JSONObject durationElement = firstRowElement.getJSONObject("duration");

         this.distance= distanceElement.getString(DISTANCE);
         this.duration= durationElement.getString(DURATION);


         Log.e(LOG_TAG, this.origin);
         Log.e(LOG_TAG, this.destination);
         Log.e(LOG_TAG, this.distance);
         Log.e(LOG_TAG, this.duration);


     } catch (JSONException e) {
         e.printStackTrace();
     }




 }


}
