package com.example.android.mydistance.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.android.mydistance.app.Data.DistanceProvider;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.MenuItemCompat;


public class RespuestaActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LIST_ID = 0;

    private String origen;
    private String destino;
    private String duracion;
    private String distancia;
    ShareActionProvider mShareActionProvider;
    String nuevaLinea=System.getProperty("line.separator");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);

        getLoaderManager().initLoader(LIST_ID, null, this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_respuesta, menu);


        MenuItem menuItem = menu.findItem(R.id.action_share);

         mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {

        }


        return true;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,this.origen+nuevaLinea+this.destino+nuevaLinea+this.distancia+nuevaLinea+this.duracion );
        return shareIntent;
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Bundle bundle = getIntent().getExtras();
        String uri = bundle.getString("Consulta");
        Uri CONTENT_URI = Uri.parse(uri);
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        data.moveToFirst();
        int colDestino = data.getColumnIndex(DistanceProvider.Distance.COL_DESTINO);
        int colOrigen = data.getColumnIndex(DistanceProvider.Distance.COL_ORIGEN);
        int colDistancia = data.getColumnIndex(DistanceProvider.Distance.COL_DISTANCIA);
        int colDuracion = data.getColumnIndex(DistanceProvider.Distance.COL_DURACION);

        do {

             this.origen=data.getString(colOrigen);
             this.destino=data.getString(colDestino);
             this.duracion=data.getString(colDuracion);
             this.distancia=data.getString(colDistancia);



        } while (data.moveToNext());

        TextView destinoTextView = (TextView) findViewById(R.id.DestinoRespuesta);
        TextView origenTextView = (TextView) findViewById(R.id.OrigenRespuesta);
        TextView distanciaTextView = (TextView) findViewById(R.id.Distancia);
        TextView tiempoTextView = (TextView) findViewById(R.id.tiempoTrayecto);

        destinoTextView.setText(this.destino);
        origenTextView.setText(this.origen);
        distanciaTextView.setText(this.distancia);
        tiempoTextView.setText(this.duracion);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
