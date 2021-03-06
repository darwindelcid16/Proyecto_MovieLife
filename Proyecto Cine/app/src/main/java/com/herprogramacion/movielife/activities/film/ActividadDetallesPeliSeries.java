
package com.herprogramacion.movielife.activities.film;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.herprogramacion.movielife.R;
import com.herprogramacion.movielife.models.Pelis;

import java.util.List;

public class ActividadDetallesPeliSeries extends AppCompatActivity {
    private static final String EXTRA_POSITION = "com.herprogramacion.cursospoint.extra.POSITION";
    private static List <Pelis> itemss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalles_peliseries);

        setToolbar();

        int position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        setupViews(itemss,position);

        FloatingActionButton fabadd = (FloatingActionButton) findViewById(R.id.addmisfavoritos);
    }

    private void setupViews(List <Pelis> items,int position) {
        TextView nombre = (TextView) findViewById(R.id.peliserie_name);
        TextView anyo = (TextView) findViewById(R.id.anyo_peliculaserie);
        ImageView imagen = (ImageView) findViewById(R.id.imageView_user);
        RatingBar rating = (RatingBar) findViewById(R.id.detail_rating);

        Pelis detallesPelis = Pelis.getESTRENOSByPosition(items,position);
        setTitle(detallesPelis.getNombre());
        nombre.setText(detallesPelis.getNombre());
        anyo.setText("" + detallesPelis.getAnyo());
        rating.setRating(detallesPelis.getRating());
        Glide.with(this).load(detallesPelis.getIdDrawable()).into(imagen);
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detallespeliseries);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                // Obtener intent de la actividad padre
                Intent upIntent = NavUtils.getParentActivityIntent(this);

                //Comprobar si ActividadDetallesPeliSeries no se creó desde ActividadPrincipal
                if (NavUtils.shouldUpRecreateTask(this, upIntent) || this.isTaskRoot()) {
                    //Construir de nuevo la tarea para ligar ambas actividades
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Terminar con el método correspondiente para Android 5.x
                    this.supportFinishAfterTransition();
                    return true;
                }
                //Dejar que el sistema maneje el comportamiendo del up button
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public  static void launch(Activity context, int position,View sharedImage,List<Pelis> items){
        Intent intent = new Intent(context,ActividadDetallesPeliSeries.class);
        intent.putExtra(EXTRA_POSITION,position);
        itemss=items;
        context.startActivity(intent);
    }
}

