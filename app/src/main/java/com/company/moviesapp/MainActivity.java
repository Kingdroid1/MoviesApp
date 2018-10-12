package com.company.moviesapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.company.moviesapp.MovieViewModel.MainViewModel;
import com.company.moviesapp.adapter.MoviesAdapter;
import com.company.moviesapp.api.Service;
import com.company.moviesapp.model.Movies;
import com.company.moviesapp.model.MoviesResponse;
import com.company.moviesapp.movieDatabase.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";

    private static String LIST_STATE = "list_state";
    public static final String LOG_TAG = MoviesAdapter.class.getName ();
    private List <Movies> moviesList;
    private AppCompatActivity appCompatActivity = MainActivity.this;
    private Parcelable savedRecyclerLayoutState;
    private ArrayList <Movies> moviesInstance = new ArrayList <> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        if (savedInstanceState != null) {
            moviesInstance = savedInstanceState.getParcelableArrayList ( LIST_STATE );
            savedRecyclerLayoutState = savedInstanceState.getParcelable ( BUNDLE_RECYCLER_LAYOUT );
            displayData ();
        } else {
            initViews ();
        }
    }

    private void displayData() {
        recyclerView = findViewById ( R.id.recycler_view );
        adapter = new MoviesAdapter ( this, moviesInstance );
        if (getActivity ().getResources ().getConfiguration ().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 3 ) );
        } else {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 6 ) );
        }
        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.setAdapter ( adapter );
        restoreLayoutManagerPosition ();
        adapter.notifyDataSetChanged ();
    }

    private void restoreLayoutManagerPosition() {
        if (savedRecyclerLayoutState != null) {
            recyclerView.getLayoutManager ().onRestoreInstanceState ( savedRecyclerLayoutState );
        }
    }

    private void initViews() {

        recyclerView = findViewById ( R.id.recycler_view );

        moviesList = new ArrayList <> ();
        adapter = new MoviesAdapter ( this, moviesList );

        if (getActivity ().getResources ().getConfiguration ().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 3 ) );
        } else {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 6 ) );
        }

        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.setAdapter ( adapter );
        loadJSON ();
    }

    private void initViews2() {

        recyclerView = findViewById ( R.id.recycler_view );

        getAllFavorite ();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState ( savedInstanceState );
        savedInstanceState.putParcelableArrayList ( LIST_STATE, moviesInstance );
        savedInstanceState.putParcelable ( BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager ().onSaveInstanceState () );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        moviesInstance = savedInstanceState.getParcelableArrayList ( LIST_STATE );
        savedRecyclerLayoutState = savedInstanceState.getParcelable ( BUNDLE_RECYCLER_LAYOUT );
        super.onRestoreInstanceState ( savedInstanceState );
    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext ();
        }
        return null;
}

    private void loadJSON() {

        String sortOrder = checkSortOrder ();

        if (sortOrder.equals ( this.getString ( R.string.most_popular ) )) {


            try {
                if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty ()) {
                    Toast.makeText ( getApplicationContext (), "Please obtain API Key ...", Toast.LENGTH_SHORT ).show ();
                    return;
                }

                OkHttpClient okHttpClient = new OkHttpClient.Builder ()

                        .build ();

                Retrofit.Builder builder = new Retrofit.Builder ()
                        .baseUrl ( "http://api.themoviedb.org/3/" )
                        .client ( okHttpClient )
                        .addConverterFactory ( GsonConverterFactory.create () );

                Retrofit retrofit = builder.build ();
                Service apiService = retrofit.create ( Service.class );
                Call <MoviesResponse> call = apiService.getPopularMovies ( BuildConfig.THE_MOVIE_DB_API_KEY );
                call.enqueue ( new Callback <MoviesResponse> () {
                    @Override
                    public void onResponse(Call <MoviesResponse> call, Response <MoviesResponse> response) {
                        if (response.isSuccessful ()) {
                            if (response.body () != null) {
                                List <Movies> movies = response.body ().getResults ();
                                moviesInstance.addAll ( movies );
                                recyclerView.setAdapter ( new MoviesAdapter ( getApplicationContext (), movies ) );
                                recyclerView.smoothScrollToPosition ( 0 );
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call <MoviesResponse> call, Throwable t) {
                        Log.d ( "Error", t.getMessage () );
                        Toast.makeText ( MainActivity.this, "Error fetching data.", Toast.LENGTH_SHORT ).show ();
                    }
                } );
            } catch (Exception e) {
                Log.d ( "Error", e.getMessage () );
                Toast.makeText ( this, e.toString (), Toast.LENGTH_SHORT ).show ();
            }

        } else if (sortOrder.equals ( this.getString ( R.string.favorite ) )) {
            initViews2 ();

        } else {

            try {
                if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty ()) {
                    Toast.makeText ( getApplicationContext (), "Please obtain API Key ...", Toast.LENGTH_SHORT ).show ();
                    return;
                }

                OkHttpClient okHttpClient = new OkHttpClient.Builder ()

                        .build ();

                Retrofit.Builder builder = new Retrofit.Builder ()
                        .baseUrl ( "http://api.themoviedb.org/3/" )
                        .client ( okHttpClient )
                        .addConverterFactory ( GsonConverterFactory.create () );

                Retrofit retrofit = builder.build ();
                Service apiService = retrofit.create ( Service.class );

                Call <MoviesResponse> call = apiService.getTopRatedMovies ( BuildConfig.THE_MOVIE_DB_API_KEY );
                call.enqueue ( new Callback <MoviesResponse> () {
                    @Override
                    public void onResponse(Call <MoviesResponse> call, Response <MoviesResponse> response) {
                        if (response.isSuccessful ()) {
                            if (response.body () != null) {
                                List <Movies> movies = response.body ().getResults ();
                                moviesInstance.addAll ( movies );
                                recyclerView.setAdapter ( new MoviesAdapter ( getApplicationContext (), movies ) );
                                recyclerView.smoothScrollToPosition ( 0 );
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call <MoviesResponse> call, Throwable t) {
                        Log.d ( "Error", t.getMessage () );
                        Toast.makeText ( MainActivity.this, "Error fetching data.", Toast.LENGTH_SHORT ).show ();
                    }
                } );
            } catch (Exception e) {
                Log.d ( "Error", e.getMessage () );
                Toast.makeText ( this, e.toString (), Toast.LENGTH_SHORT ).show ();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater ().inflate ( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId ()){
            case R.id.menu_settings:
                Intent intent = new Intent ( this, SettingsActivity.class );
                startActivity ( intent );
                return true;
            default:
                return super.onOptionsItemSelected ( item );
        }
    }

    private String checkSortOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences ( this );
        String sortOrder = preferences.getString (
                this.getString ( R.string.sort_order_key ),
                this.getString ( R.string.most_popular )
        );

        return sortOrder;
    }

    private void getAllFavorite() {
        MainViewModel mainViewModel = ViewModelProviders.of ( this ).get ( MainViewModel.class );
        mainViewModel.getFavoriteMovie ().observe ( this, new Observer <List <MovieEntity>> () {
            @Override
            public void onChanged(@Nullable List <MovieEntity> movieEntities) {
                List <Movies> movies = new ArrayList <> ();
                for (MovieEntity movieEntity : movieEntities) {
                    Movies movie = new Movies ();
                    movie.setId ( movieEntity.getMovieId () );
                    movie.setOverview ( movieEntity.getOverview () );
                    movie.setOriginalTitle ( movieEntity.getTitle () );
                    movie.setPosterPath ( movieEntity.getPosterPath () );
                    movie.setVoteAverage ( movieEntity.getUserRating () );

                    movies.add ( movie );
                }

                adapter.setMovies ( movies );
            }

        } );
    }
}
