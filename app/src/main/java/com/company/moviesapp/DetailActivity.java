package com.company.moviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.moviesapp.MovieViewModel.AppExecutors;
import com.company.moviesapp.adapter.ReviewsAdapter;
import com.company.moviesapp.adapter.TrailerAdapter;
import com.company.moviesapp.api.RetrofitClient;
import com.company.moviesapp.api.Service;
import com.company.moviesapp.model.Movies;
import com.company.moviesapp.model.ReviewResult;
import com.company.moviesapp.model.Reviews;
import com.company.moviesapp.model.Trailer;
import com.company.moviesapp.model.TrailerResponse;
import com.company.moviesapp.movieDatabase.MovieDB;
import com.company.moviesapp.movieDatabase.MovieEntity;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {


    private final AppCompatActivity appCompatActivity = DetailActivity.this;
    // @BindView(R.id.title)
    //String nameOfMovie;
    @BindView(R.id.plot_synopsis)
    TextView plotSynopsis;
    @BindView(R.id.user_rating)
    TextView userRating;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.thumbnail_image_header)
    ImageView imageView;
    @BindView(R.id.recycler_view1)
    RecyclerView recyclerView;
    List <MovieEntity> movieEntities = new ArrayList <> ();
    boolean exists;
    int movie_id;
    Movies movies;
    String thumbnail, movieName, synopsis, rating, dateOfRelease;
    private TrailerAdapter trailerAdapter;
    private List <Trailer> trailerList;
    private Movies favoriteMovies;
    private MovieDB movieDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_detail );
        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        ButterKnife.bind ( this );

        movieDB = MovieDB.getInstance ( getApplicationContext () );

        Intent intentThatStartedThisActivity = getIntent ();
        if (intentThatStartedThisActivity.hasExtra ( "movies" )) {
            movies = getIntent ().getParcelableExtra ( "movies" );

            thumbnail = movies.getPosterPath ();
            movieName = movies.getOriginalTitle ();
            synopsis = movies.getOverview ();
            rating = Double.toString ( movies.getVoteAverage () );
            dateOfRelease = movies.getReleaseDate ();
            movie_id = movies.getId ();

            String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

            Picasso.get ()
                    .load ( poster )
                    .noFade ()
                    .placeholder ( getResources ().getDrawable ( R.drawable.ic_image_black ) )
                    .into ( imageView );

            // nameOfMovie.setText ( nameOfMovie );
            plotSynopsis.setText ( synopsis );
            userRating.setText ( rating );
            releaseDate.setText ( dateOfRelease );

            ((CollapsingToolbarLayout) findViewById ( R.id.collapsing_toolbar )).setTitle ( movieName );

        } else {
            Toast.makeText ( this, "No API Data", Toast.LENGTH_SHORT ).show ();
        }

        checkStatus ( movieName );
        initViews ();
    }


    public void initViews() {
        trailerList = new ArrayList <> ();
        trailerAdapter = new TrailerAdapter ( this, trailerList );

        ButterKnife.bind ( this );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getApplicationContext () );
        recyclerView.setLayoutManager ( layoutManager );
        recyclerView.setAdapter ( trailerAdapter ); //corrected
        trailerAdapter.notifyDataSetChanged ();

        loadJSON ();
        loadReview ();
    }

    private void loadJSON() {

        try {
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty ()) {
                Toast.makeText ( getApplicationContext (), "Please obtain your API Key ...", Toast.LENGTH_SHORT ).show ();
                return;
            }
            RetrofitClient RetrofitClient = new RetrofitClient ();
            Service apiService = com.company.moviesapp.api.RetrofitClient.getRetrofit ().create ( Service.class );
            Call <TrailerResponse> call = apiService.getMovieTrailer ( movie_id, BuildConfig.THE_MOVIE_DB_API_KEY );
            call.enqueue ( new Callback <TrailerResponse> () {
                @Override
                public void onResponse(Call <TrailerResponse> call, Response <TrailerResponse> response) {
                    if (response.isSuccessful ()) {
                        if (response.body () != null) {
                            List <Trailer> trailer = response.body ().getResults ();
                            MultiSnapRecyclerView recyclerView = findViewById ( R.id.recycler_view1 );
                            LinearLayoutManager firstManager = new LinearLayoutManager ( getApplicationContext (), LinearLayoutManager.VERTICAL, false );
                            recyclerView.setLayoutManager ( firstManager );
                            recyclerView.setAdapter ( new TrailerAdapter ( getApplicationContext (), trailer ) );
                            recyclerView.smoothScrollToPosition ( 0 );
                        }
                    }
                }

                @Override
                public void onFailure(Call <TrailerResponse> call, Throwable t) {
                    Log.d ( "Error", t.getMessage () );
                    Toast.makeText ( DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT ).show ();
                }
            } );
        } catch (Exception e) {
            Log.d ( "Error", e.getMessage () );
            Toast.makeText ( this, e.toString (), Toast.LENGTH_SHORT ).show ();

        }
    }

    private void loadReview() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty ()) {
                Toast.makeText ( getApplicationContext (), "Please get your API Key", Toast.LENGTH_SHORT ).show ();
                return;
            } else {
                RetrofitClient RetrofitClient = new RetrofitClient ();
                Service apiService = com.company.moviesapp.api.RetrofitClient.getRetrofit ().create ( Service.class );
                Call <Reviews> call = apiService.getReview ( movie_id, BuildConfig.THE_MOVIE_DB_API_KEY );

                call.enqueue ( new Callback <Reviews> () {
                    @Override
                    public void onResponse(Call <Reviews> call, Response <Reviews> response) {
                        if (response.isSuccessful ()) {
                            if (response.body () != null) {
                                List <ReviewResult> reviewResults = response.body ().getResults ();
                                MultiSnapRecyclerView recyclerView2 = findViewById ( R.id.review_recyclerview );
                                LinearLayoutManager firstManager = new LinearLayoutManager ( getApplicationContext (), LinearLayoutManager.VERTICAL, false );
                                recyclerView2.setLayoutManager ( firstManager );
                                recyclerView2.setAdapter ( new ReviewsAdapter ( getApplicationContext (), reviewResults ) );
                                recyclerView2.smoothScrollToPosition ( 0 );
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call <Reviews> call, Throwable t) {

                    }
                } );
            }

        } catch (Exception e) {
            Log.d ( "Error", e.getMessage () );
            Toast.makeText ( this, "unable to fetch data", Toast.LENGTH_SHORT ).show ();
        }
    }

    public void saveFavorite() {
        Double rate = movies.getVoteAverage ();
        final MovieEntity movieEntity = new MovieEntity ( movie_id, movieName, rate, thumbnail, synopsis );
        AppExecutors.getInstance ().diskIO ().execute ( new Runnable () {
            @Override
            public void run() {
                movieDB.MovieDao ().insertFavoriteMovie ( movieEntity );
            }
        } );
    }

    private void deleteFavorite(final int movie_id) {
        AppExecutors.getInstance ().diskIO ().execute ( new Runnable () {
            @Override
            public void run() {
                movieDB.MovieDao ().deleteFavoriteMovieById ( movie_id );
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater ().inflate ( R.menu.detail_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId ()) {
            case R.id.share:
                shareContent ();

                return true;
        }

        return super.onOptionsItemSelected ( item );
    }

    private void shareContent() {

        Bitmap bitmap = getBitmapFromView ( imageView );
        try {
            File file = new File ( this.getExternalCacheDir (), "logicchip.png" );
            FileOutputStream fOut = new FileOutputStream ( file );
            bitmap.compress ( Bitmap.CompressFormat.PNG, 100, fOut );
            fOut.flush ();
            fOut.close ();
            file.setReadable ( true, false );
            final Intent intent = new Intent ( android.content.Intent.ACTION_SEND );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.putExtra ( Intent.EXTRA_TEXT, movieName );
            intent.putExtra ( Intent.EXTRA_STREAM, Uri.fromFile ( file ) );
            intent.setType ( "image/png" );
            startActivity ( Intent.createChooser ( intent, "Share image via" ) );
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap ( view.getWidth (), view.getHeight (), Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas ( returnedBitmap );
        Drawable bgDrawable = view.getBackground ();
        if (bgDrawable != null) {
            bgDrawable.draw ( canvas );
        } else {
            canvas.drawColor ( Color.WHITE );
        }
        view.draw ( canvas );
        return returnedBitmap;
    }

    @SuppressLint("StaticFieldLeak")
    private void checkStatus(final String movieName) {
        final MaterialFavoriteButton materialFavoriteButton = findViewById ( R.id.favorite_button );
        new AsyncTask <Void, Void, Void> () {
            @Override
            protected Void doInBackground(Void... params) {
                movieEntities.clear ();
                movieEntities = movieDB.MovieDao ().loadAll ( movieName );
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute ( aVoid );
                if (movieEntities.size () > 0) {
                    materialFavoriteButton.setFavorite ( true );
                    materialFavoriteButton.setOnFavoriteChangeListener (
                            new MaterialFavoriteButton.OnFavoriteChangeListener () {
                                @Override
                                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                    if (favorite) {
                                        saveFavorite ();
                                        Snackbar.make ( buttonView, "Added to Favorite",
                                                Snackbar.LENGTH_SHORT ).show ();
                                    } else {
                                        deleteFavorite ( movie_id );
                                        Snackbar.make ( buttonView, "Removed from Favorite",
                                                Snackbar.LENGTH_SHORT ).show ();
                                    }
                                }
                            } );


                } else materialFavoriteButton.setOnFavoriteChangeListener (
                        new MaterialFavoriteButton.OnFavoriteChangeListener () {
                            @Override
                            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                if (favorite) {
                                    saveFavorite ();
                                    Snackbar.make ( buttonView, "Added to Favorite",
                                            Snackbar.LENGTH_SHORT ).show ();
                                } else {
                                    int movie_id = Objects.requireNonNull ( getIntent ().getExtras () ).getInt ( "id" );
                                    deleteFavorite ( movie_id );
                                    Snackbar.make ( buttonView, "Removed from Favorite",
                                            Snackbar.LENGTH_SHORT ).show ();
                                }
                            }
                        } );
            }
        }.execute ();
    }
}

