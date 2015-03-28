package com.example.android.mydistance.app.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alberto on 27/03/2015.
 */
public class DistanceSqliteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "distancedb";

    //Sentencia SQL para crear la tabla del historial de distancias
    String sqlCreate = "CREATE TABLE DistanciaTask " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " Origen TEXT, " +
            " Destino TEXT, " +
            " Distancia TEXT," +
            " Duracion TEXT )";


    public DistanceSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);

        //Insertamos 15 Distancias de ejemplo
        for(int i=1; i<=7; i++)
        {
            //Generamos los datos de muestra
            String origen = "Sevilla, Sevilla, España"+ i;
            String destino = "Córdoba, Córdoba, España" + i;
            String distancia = "143 km";
            String duracion = "1h 35 min";

            //Insertamos los datos en la tabla Clientes
            db.execSQL("INSERT INTO Distancia (Origen, Destino, Distancia, Duracion) " +
                    "VALUES ('" + origen + "', '" + destino +"', '" + distancia + "', '"+ duracion +"')");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Clientes");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}