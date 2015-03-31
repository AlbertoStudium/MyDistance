package com.example.android.mydistance.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.android.mydistance.app.Data.DistanceProvider;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,Escuchador {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LIST_ID = 0;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> uriSelected = new ArrayList<String>();
    private String[] arrayDestino;
    private String[] arrayOrigen;
    private List<String> DistanceForecast;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(LIST_ID, null, this);
        View enviar = findViewById(R.id.enviar);
       // AccesProvider acces = new AccesProvider(getApplicationContext());

        enviar.setOnClickListener(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        EditText Origen = (EditText) findViewById(R.id.OrigenCliente);
        EditText Destino = (EditText) findViewById(R.id.DestinoCliente);
        String OrigenString = Origen.getText().toString();
        String DestinoString = Destino.getText().toString();



        DistanceTask distanceTask = new DistanceTask(this, this);
       distanceTask.execute(OrigenString, DestinoString);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri CONTENT_URI = DistanceProvider.CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, null, null, null, "_ID DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToFirst();

        int colOrigen = cursor.getColumnIndex(DistanceProvider.Distance.COL_ORIGEN);
        int colDestino = cursor.getColumnIndex(DistanceProvider.Distance.COL_DESTINO);
        int colId = cursor.getColumnIndex(DistanceProvider.Distance._ID);

        for(int i =0;i<=10;i++) {

            this.arrayOrigen = cursor.getString(colOrigen).split(",");
            this.arrayDestino = cursor.getString(colDestino).split(",");
            this.id = cursor.getInt(colId);
            this.uriSelected.add(DistanceProvider.CONTENT_URI+"/"+String.valueOf(id));
            data.add(this.arrayOrigen[0]+", "+this.arrayOrigen[1]+" / "+this.arrayDestino[0]+", "+this.arrayDestino[1]);
            cursor.moveToNext();

        }

        this.DistanceForecast = new ArrayList<String>(this.data);

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.single_row_from_list, // The name of the layout ID.
                        R.id.SingleRow, // The ID of the textview to populate.
                        this.DistanceForecast);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(forecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              //  Log.d(LOG_TAG,uri.toString());
                Intent intent = new Intent(getApplicationContext(),RespuestaActivity.class);
                intent.putExtra("Consulta", uriSelected.get(position));
                startActivity(intent);

            }
        });


        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {

        }

    @Override
    public void heTerminado(Uri uri) {

        if (uri!=null) {

            Log.d(LOG_TAG,uri.toString());
            Intent intent = new Intent(this,RespuestaActivity.class);
            intent.putExtra("Consulta", uri.toString());
            startActivity(intent);
        }else{

            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            "Invalid Request", Toast.LENGTH_SHORT);

            toast.show();
        }

    }
}

