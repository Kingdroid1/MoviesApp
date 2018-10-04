package com.company.moviesapp.movieDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.widget.ImageView;
import android.widget.TextView;


@Entity(tableName = "moviesFavorite")
public class MovieEntity {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "movieId")
    private int movieId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "posterPath")
    private String posterPath;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "userRating")
    private Double userRating;

    @ColumnInfo(name = "releaseDate")
    private String releaseDate;


    public MovieEntity(int id, int movieId, String title, String posterPath, String overview, Double userRating, String releaseDate) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public MovieEntity(int movieId, String title, Double userRating, String posterPath, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.userRating = userRating;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public MovieEntity(int movieId, String nameOfMovie, Double rate, ImageView imageView, TextView plotSynopsis) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
