package com.company.moviesapp.MovieViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.company.moviesapp.movieDatabase.MovieDB;
import com.company.moviesapp.movieDatabase.MovieEntity;

public class AddMovieFavorite extends ViewModel {

    private LiveData <MovieEntity> movieEntityLiveData;

    public AddMovieFavorite(MovieDB movieDB, int movieId) {
        movieEntityLiveData = movieDB.MovieDao ().loadFavoriteMovieById ( movieId );
    }

    public LiveData <MovieEntity> getMovieEntityLiveData() {
        return movieEntityLiveData;
    }
}
