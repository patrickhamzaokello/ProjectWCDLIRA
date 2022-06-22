package com.pkasemer.worshipcenterdowntown.Apis;



import com.pkasemer.worshipcenterdowntown.Models.HomeBase;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {




    @GET("home_feed.php")
    Call<HomeBase> getHomeFeedRequest(
            @Query("page") int pageIndex
    );





}