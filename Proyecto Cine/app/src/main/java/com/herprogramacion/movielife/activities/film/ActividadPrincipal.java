package com.herprogramacion.movielife.activities.film;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.herprogramacion.movielife.R;
import com.herprogramacion.movielife.activities.database.firebase.FireBaseActivity;
import com.herprogramacion.movielife.activities.database.sqlite.TareasSQLiteActivity;
import com.herprogramacion.movielife.activities.maps.LocationActivity;
import com.herprogramacion.movielife.fragments.FragmentCines;
import com.herprogramacion.movielife.fragments.FragmentoCuenta;
import com.herprogramacion.movielife.fragments.FragmentoMisFavoritos;
import com.herprogramacion.movielife.fragments.FragmentoPeliSeries;

import java.util.Locale;

public class ActividadPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    public static String search;
    public static final String BOOK_DETAIL_KEY = "book";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            actionBar.setTitle(getString(R.string.app_name));
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(1));
        }

    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.item_estrenos:
                fragmentoGenerico = new FragmentoPeliSeries();
                break;
            case R.id.item_cuenta:
                fragmentoGenerico = new FragmentoCuenta();
                break;
            case R.id.item_mas_info:
                fragmentoGenerico = new FragmentoMisFavoritos();
                break;
            case R.id.item_mis_favoritos:
                startActivity(new Intent(this, SavedFilmsActivity.class));
                break;
            case R.id.item_mis_favoritos_cines:
                startActivity(new Intent(this, SavedCinesActivity.class));
                break;
            case R.id.item_configuracion:
                startActivity(new Intent(this, ActividadConfiguracion.class));
                break;
            case R.id.item_locate:
                fragmentoGenerico = new FragmentCines();
                break;
            case R.id.item_peliculas:
                startActivity(new Intent(this, Search_Activity.class));
                break;
            case R.id.item_crud:
                startActivity(new Intent(this, TareasSQLiteActivity.class));
                break;
            case R.id.item_crud_firebase:
                startActivity(new Intent(this, FireBaseActivity.class));
                break;
            case R.id.item_google_maps:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.en:
                change_lang("en");
                break;
            case R.id.es:
                change_lang("es");
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //  searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                if (query != null) {
                    search = query;
                    // Reset SearchView
                    searchView.clearFocus();
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    searchItem.collapseActionView();
                    startactividad();
                    return true;
                } else
                    return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    public void startactividad() {
        startActivity(new Intent(this, Search_Activity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void change_lang(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        //String lang_default = Locale.getDefault().getLanguage();
        //Toast toast1 = Toast.makeText(getApplicationContext(), lang_default, Toast.LENGTH_SHORT);
        //toast1.show();

        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        //getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

       finish();
       Intent refresh = new Intent(this, ActividadPrincipal.class);
       startActivity(refresh);
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        change_lang(language);
    }


    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    }
