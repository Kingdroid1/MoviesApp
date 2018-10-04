package com.company.moviesapp.movieDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM moviesFavorite ORDER BY movieId")
    LiveData <List <MovieEntity>> loadAllFavoriteMovie();

    @Query("SELECT * FROM moviesFavorite WHERE title = :title")
    List <MovieEntity> loadAll(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(MovieEntity movieEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(MovieEntity movieEntity);

    @Delete
    void deleteFavoriteMovie(MovieEntity movieEntity);

    @Query("DELETE FROM moviesFavorite WHERE movieId = :movieId")
    void deleteFavoriteMovieById(int movieId);

    @Query("SELECT * FROM moviesFavorite WHERE movieId = :movieId")
    LiveData <MovieEntity> loadFavoriteMovieById(int movieId);

}
