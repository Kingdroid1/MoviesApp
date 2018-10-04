package com.company.moviesapp.MovieViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.company.moviesapp.movieDatabase.MovieDB;

public class AddMovieFavoriteFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDB movieDB;
    private final int mFavouriteId;

    public AddMovieFavoriteFactory(MovieDB movieDB, int mFavouriteId) {
        this.movieDB = movieDB;
        this.mFavouriteId = mFavouriteId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class <T> modelClass) {
        return (T) new AddMovieFavorite ( movieDB, mFavouriteId );
    }
}
