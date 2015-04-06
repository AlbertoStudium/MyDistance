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
    public static final String TABLE_NAME = "DistanciaTask";

    //Sentencia SQL para crear la tabla del historial de distancias
    private String sqlCreate = "CREATE TABLE " + TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " Origen TEXT, " +
            " Destino TEXT, " +
            " Distancia TEXT," +
            " Modo TEXT," +
            " Duracion TEXT )";


    public DistanceSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(this.sqlCreate);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        //Se crea la nueva versión de la tabla
        db.execSQL(this.sqlCreate);
    }
}
