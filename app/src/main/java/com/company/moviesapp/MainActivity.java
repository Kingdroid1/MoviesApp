package com.company.moviesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.company.moviesapp.movieDatabase.MovieDao;
import com.company.moviesapp.movieDatabase.MovieEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String LIST_STATE = "list_state";
    MovieDao movieDao;
    public static final String LOG_TAG = MoviesAdapter.class.getName ();
    int cacheSize = 10 * 1024 * 1024; // 10 MiB
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
    private void initViews(){
        progressDialog = new ProgressDialog ( this );
        progressDialog.setMessage ( "Fetching movies ..." );
        progressDialog.setCancelable ( false );
        progressDialog.show ();

        recyclerView = findViewById ( R.id.recycler_view );

        moviesList = new ArrayList <> (  );
        //adapter = new MoviesAdapter (this, moviesList);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 3 ) );
        }else {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 6 ) );
        }

        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.setAdapter ( adapter );
        adapter.notifyDataSetChanged();
        movieDao = new MovieDao () {
            @Override
            public LiveData <List <MovieEntity>> loadAllFavoriteMovie() {
                return null;
            }

            @Override
            public List <MovieEntity> loadAll(String title) {
                return null;
            }

            @Override
            public void insertFavoriteMovie(MovieEntity movieEntity) {

            }

            @Override
            public void updateFavoriteMovie(MovieEntity movieEntity) {

            }

            @Override
            public void deleteFavoriteMovie(MovieEntity movieEntity) {

            }

            @Override
            public void deleteFavoriteMovieById(int movie_id) {

            }

            @Override
            public LiveData <MovieEntity> loadFavoriteMovieById(int movie_id) {
                return null;
            }
        };

        swipeRefreshLayout = findViewById ( R.id.main_content );
        swipeRefreshLayout.setColorSchemeResources ( android.R.color.holo_orange_dark );
        swipeRefreshLayout.setOnRefreshListener ( new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                initViews ();
                Toast.makeText ( MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT ).show ();
            }
        } );

        checkSortOrder ();
    }

    private void initViews2() {
        progressDialog = new ProgressDialog ( this );
        progressDialog.setMessage ( "Fetching movies ..." );
        progressDialog.setCancelable ( false );
        progressDialog.show ();

        recyclerView = findViewById ( R.id.recycler_view );

        moviesList = new ArrayList <> ();
        // adapter = new MoviesAdapter ( this, moviesList );

        if (getActivity ().getResources ().getConfiguration ().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 3 ) );
        } else {
            recyclerView.setLayoutManager ( new GridLayoutManager ( this, 6 ) );
        }

        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.setAdapter ( adapter );
        adapter.notifyDataSetChanged ();
        movieDao = new MovieDao () {
            @Override
            public LiveData <List <MovieEntity>> loadAllFavoriteMovie() {
                return null;
            }

            @Override
            public List <MovieEntity> loadAll(String title) {
                return null;
            }

            @Override
            public void insertFavoriteMovie(MovieEntity movieEntity) {

            }

            @Override
            public void updateFavoriteMovie(MovieEntity movieEntity) {

            }

            @Override
            public void deleteFavoriteMovie(MovieEntity movieEntity) {

            }

            @Override
            public void deleteFavoriteMovieById(int movieId) {

            }

            @Override
            public LiveData <MovieEntity> loadFavoriteMovieById(int movieId) {
                return null;
            }
        };

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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService ( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo ();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected ();
    }

    private void loadJSON(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty ()){
                Toast.makeText ( getApplicationContext (), "Please obtain API Key ...", Toast.LENGTH_SHORT).show ();
                progressDialog.dismiss ();
                return;
            }

            Cache cache = new Cache ( getCacheDir (), cacheSize );

            OkHttpClient okHttpClient = new OkHttpClient.Builder ()
                    .cache ( cache )
                    .addInterceptor ( new Interceptor () {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain)
                                throws IOException {
                            Request request = chain.request ();
                            if (!isNetworkAvailable ()) {
                                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                                request = request
                                        .newBuilder ()
                                        .header ( "Cache-Control", "public, only-if-cached, max-stale=" + maxStale )
                                        .build ();
                            }
                            return chain.proceed ( request );
                        }
                    } )
                    .build ();

            Retrofit.Builder builder = new Retrofit.Builder ()
                    .baseUrl ( "http://api.themoviedb.org/3/" )
                    .client ( okHttpClient )
                    .addConverterFactory ( GsonConverterFactory.create () );

            Retrofit retrofit = builder.build ();
            //RetrofitClient retrofitClient = new RetrofitClient ();
            Service apiService = retrofit.create ( Service.class );
            Call<MoviesResponse> call = apiService.getPopularMovies ( BuildConfig.THE_MOVIE_DB_API_KEY );
            call.enqueue ( new Callback <MoviesResponse> () {
                @Override
                public void onResponse(Call <MoviesResponse> call, Response<MoviesResponse> response) {
                    if (response.isSuccessful ()) {
                        Toast.makeText ( MainActivity.this, "the value is " + moviesInstance.size (), Toast.LENGTH_SHORT ).show ();
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
                    Log.d("Error", t.getMessage ());
                    Toast.makeText ( MainActivity.this, "Error fetching data.", Toast.LENGTH_SHORT ).show ();
                }
            } );
        }catch (Exception e){
            Log.d("Error", e.getMessage ());
            Toast.makeText ( this, e.toString (), Toast.LENGTH_SHORT ).show ();
        }
    }

    private void loadJSON1(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty ()){
                Toast.makeText ( getApplicationContext (), "Please obtain API Key ...", Toast.LENGTH_SHORT).show ();
                progressDialog.dismiss ();
                return;
            }

            Cache cache = new Cache ( getCacheDir (), cacheSize );

            OkHttpClient okHttpClient = new OkHttpClient.Builder ()
                    .cache ( cache )
                    .addInterceptor ( new Interceptor () {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain)
                                throws IOException {
                            Request request = chain.request ();
                            if (!isNetworkAvailable ()) {
                                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                                request = request
                                        .newBuilder ()
                                        .header ( "Cache-Control", "public, only-if-cached, max-stale=" + maxStale )
                                        .build ();
                            }
                            return chain.proceed ( request );
                        }
                    } )
                    .build ();

            Retrofit.Builder builder = new Retrofit.Builder ()
                    .baseUrl ( "http://api.themoviedb.org/3/" )
                    .client ( okHttpClient )
                    .addConverterFactory ( GsonConverterFactory.create () );

            Retrofit retrofit = builder.build ();
            Service apiService = retrofit.create ( Service.class );

            //RetrofitClient retrofitClient = new RetrofitClient ();
            Call<MoviesResponse> call = apiService.getTopRatedMovies( BuildConfig.THE_MOVIE_DB_API_KEY );
            call.enqueue ( new Callback <MoviesResponse> () {
                @Override
                public void onResponse(Call <MoviesResponse> call, Response<MoviesResponse> response) {
                    if (response.isSuccessful ()) {
                        if (response.body () != null) {
                            List <Movies> movies = response.body ().getResults ();
                            recyclerView.setAdapter ( new MoviesAdapter ( getApplicationContext (), movies ) );
                            if (swipeRefreshLayout.isRefreshing ()) {
                                swipeRefreshLayout.setRefreshing ( false );
                            }
                            progressDialog.dismiss ();
                        }
                    }
                }

                @Override
                public void onFailure(Call <MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage ());
                    Toast.makeText ( MainActivity.this, "Error fetching data.", Toast.LENGTH_SHORT ).show ();
                }
            } );
        }catch (Exception e){
            Log.d("Error", e.getMessage ());
            Toast.makeText ( this, e.toString (), Toast.LENGTH_SHORT ).show ();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d ( LOG_TAG, "Preference updated" );
        checkSortOrder();
    }

    private void checkSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences ( this );
        String sortOrder = preferences.getString (
                this.getString ( R.string.sort_order_key ),
                this.getString ( R.string.most_popular )
        );
        if (sortOrder.equals ( this.getString ( R.string.most_popular ) )){
            Log.d ( LOG_TAG,"Sorting by most popular" );
            loadJSON ();
        } else if (sortOrder.equals ( this.getString ( R.string.favorite ) )) {
            Log.d ( LOG_TAG, "Sorting by favorite" );
            initViews2 ();
        } else {
            Log.d ( LOG_TAG,"Sorting by vote average" );
            loadJSON1 ();
        }
    }

    @Override
    public void onResume(){
        super.onResume ();
        if (moviesList.isEmpty ()){
            checkSortOrder ();
        } else {
            checkSortOrder ();
        }
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
