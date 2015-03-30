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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.mydistance.app.Data.AccesProvider;
import com.example.android.mydistance.app.Data.DistanceProvider;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,Escuchador {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LIST_ID = 0;
    private ArrayList<String> data = new ArrayList<String>();
    private String[] arrayDestino;
    private String[] arrayOrigen;
    private List<String> DistanceForecast;

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
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToFirst();

        int colOrigen = cursor.getColumnIndex(DistanceProvider.Distance.COL_ORIGEN);
        int colDestino = cursor.getColumnIndex(DistanceProvider.Distance.COL_DESTINO);
       // int colDuracion = cursor.getColumnIndex(DistanceProvider.Distance.COL_DURACION);
       // int colDistancia = cursor.getColumnIndex(DistanceProvider.Distance.COL_DISTANCIA);
       // int colId = cursor.getColumnIndex(DistanceProvider.Distance._ID);

        do {

            this.arrayOrigen = cursor.getString(colOrigen).split(",");
            this.arrayDestino = cursor.getString(colDestino).split(",");
            data.add(this.arrayOrigen[0]+", "+this.arrayOrigen[1]+" / "+this.arrayDestino[0]+", "+this.arrayDestino[1]);
            // data.add(String.valueOf(cursor.getInt(colId)));

        } while (cursor.moveToNext());

        this.DistanceForecast = new ArrayList<String>(this.data);

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.single_row_from_list, // The name of the layout ID.
                        R.id.SingleRow, // The ID of the textview to populate.
                        this.DistanceForecast);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(forecastAdapter);


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

