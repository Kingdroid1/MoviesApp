package com.company.moviesapp.api;

import com.company.moviesapp.model.MoviesResponse;
import com.company.moviesapp.model.Reviews;
import com.company.moviesapp.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query ( "api_key" ) String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query ( "api_key" ) String apiKey);

    @GET("movie/{movie_id}/videos")
    Call <TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    //Reviews
    @GET("movie/{movie_id}/reviews")
    Call <Reviews> getReview(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
