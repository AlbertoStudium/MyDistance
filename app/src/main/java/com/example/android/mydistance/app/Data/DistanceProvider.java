package com.example.android.mydistance.app.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alberto on 27/03/2015.
 */
public class DistanceProvider extends ContentProvider {

    private static final String TABLA_DISTANCE = "distancedb";
    private DistanceSqliteHelper Dishelper;
    //Definición del CONTENT_URI
    private static final String uri = "content://com.example.android.mydistance.app/"+TABLA_DISTANCE;

    public static final Uri CONTENT_URI = Uri.parse(uri);

    //UriMatcher
    private static final int DESTINO = 1;
    private static final int DESTINO_ID = 2;
    private static final UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI("com.example.android.mydistance.app", TABLA_DISTANCE, DESTINO);
        uriMatcher.addURI("com.example.android.mydistance.app", TABLA_DISTANCE+"/#", DESTINO_ID);
    }


    @Override
    public boolean onCreate() {

        Dishelper = new DistanceSqliteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == DESTINO_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = Dishelper.getWritableDatabase();

        // DEVOLVEMOS UN CURSOR CON LOS RESULTADOS
        Cursor c = db.query(TABLA_DISTANCE, projection, where,
                selectionArgs, null, null, sortOrder);

        return c;
    }

    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match)
        {
            case DESTINO:
                return "vnd.android.cursor.dir/vnd.com.example.android.mydistance.app/"+TABLA_DISTANCE;
            case DESTINO_ID:
                return "vnd.android.cursor.item/vnd.com.example.android.mydistance.app/"+TABLA_DISTANCE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long regId = 1;

        SQLiteDatabase db = Dishelper.getWritableDatabase();

        regId = db.insert(TABLA_DISTANCE, null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // RECOGEMOS LAS FILAS AFECTADAS Y LAS DEVOLVEMOS
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == DESTINO_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = Dishelper.getWritableDatabase();

        cont = db.delete(TABLA_DISTANCE, where, selectionArgs);

        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // RECOGEMOS LAS FILAS AFECTADAS Y LAS DEVOLVEMOS
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == DESTINO_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = Dishelper.getWritableDatabase();

        cont = db.update(TABLA_DISTANCE, values, where, selectionArgs);

        return cont;

    }

    //Clase interna para declarar las constantes de columna
    public static final class Distance implements BaseColumns
    {
        private Distance() {}

        //Nombres de columnas
        public static final String COL_ORIGEN = "Origen";
        public static final String COL_DESTINO = "Destino";
        public static final String COL_DISTANCIA = "Distancia";
        public static final String COL_DURACION = "Duracion";
    }
}
