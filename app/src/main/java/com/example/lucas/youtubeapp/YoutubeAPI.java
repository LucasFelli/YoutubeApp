package com.example.lucas.youtubeapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lucas on 22/03/2018.
 */
public interface YoutubeAPI {
    String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    String API_KEY = "AIzaSyDR75Q1QIiRNL2AmpGx687PxurFGaYGF4s";

    @GET("search?part=snippet&key=AIzaSyDR75Q1QIiRNL2AmpGx687PxurFGaYGF4s")
    Call<YoutubeCallAnswer> search(@Query("q") String query);
}