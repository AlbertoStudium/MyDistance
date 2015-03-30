package com.example.android.mydistance.app.Data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.example.android.mydistance.app.Data.DistanceProvider.Distance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alberto on 28/03/2015.
 */
public class AccesProvider {


    private String[] arrayDestino;
    private String[] arrayOrigen;

    private String[] columnsDistance = new String[] {
            Distance._ID,
            Distance.COL_ORIGEN,
            Distance.COL_DESTINO,
            Distance.COL_DISTANCIA,
            Distance.COL_DURACION };

    private Uri distanceUri =  DistanceProvider.CONTENT_URI;

    private ContentResolver cr;

    private Cursor cursor;

    private ArrayList<String> data = new ArrayList<String>();

    public AccesProvider(Context context) {
        cr = context.getContentResolver();
    }



    public ArrayList<String> getHistory(){


        //recogemos los datos exsistentes de la base de datos
        cursor = cr.query(distanceUri,
                columnsDistance, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        if (cursor.moveToFirst()) {
            int colOrigen = cursor.getColumnIndex(Distance.COL_ORIGEN);
            int colDestino = cursor.getColumnIndex(Distance.COL_DESTINO);
            int colDuracion = cursor.getColumnIndex(Distance.COL_DURACION);
            int colDistancia = cursor.getColumnIndex(Distance.COL_DISTANCIA);
            int colId = cursor.getColumnIndex(Distance._ID);



            do {

                 this.arrayOrigen = cursor.getString(colOrigen).split(",");
                 this.arrayDestino = cursor.getString(colDestino).split(",");
                data.add(this.arrayOrigen[0]+", "+this.arrayOrigen[1]+" / "+this.arrayDestino[0]+", "+this.arrayDestino[1]);
               // data.add(String.valueOf(cursor.getInt(colId)));


            } while (cursor.moveToNext());

        }
        return this.data;
    }
}
