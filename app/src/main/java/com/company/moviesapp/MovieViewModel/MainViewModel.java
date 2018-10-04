package com.company.moviesapp.MovieViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.company.moviesapp.movieDatabase.MovieDB;
import com.company.moviesapp.movieDatabase.MovieEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant For Logging
    private static final String TAG = MainViewModel.class.getSimpleName ();

    private LiveData <List <MovieEntity>> favoriteMovie;

    public MainViewModel(@NonNull Application application) {
        super ( application );
        MovieDB movieDB = MovieDB.getInstance ( this.getApplication () );
        Log.d ( TAG, "Actively retrieving favorite movie data from database" );
        favoriteMovie = movieDB.MovieDao ().loadAllFavoriteMovie ();
    }

    public LiveData <List <MovieEntity>> getFavoriteMovie() {
        return favoriteMovie;
    }
}
