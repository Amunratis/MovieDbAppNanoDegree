package com.example.sirth.moviedbappnanodegree.networkUtil;

import com.example.sirth.moviedbappnanodegree.dataModel.MoviePages;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieReview;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sirth on 16/03/2018.
 */

public interface RetrofitService  {

    @GET("movie/popular")
    Call<MoviePages> getPopularMovies(@Query("api_key")String apikey);
    @GET("movie/top_rated")
    Call<MoviePages> getTopRated(@Query("api_key")String apikey);

    @GET("movie/{id}/videos")
    Call<MovieTrailer> getTrailers(@Path("id") int id, @Query("api_key")String apikey);
    @GET("movie/{id}/reviews")
    Call<MovieReview> getReviews(@Path("id") int id, @Query("api_key")String apikey);

}
