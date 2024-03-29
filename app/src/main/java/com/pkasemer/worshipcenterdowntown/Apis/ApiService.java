package com.pkasemer.worshipcenterdowntown.Apis;



import com.pkasemer.worshipcenterdowntown.Models.AlertsPage;
import com.pkasemer.worshipcenterdowntown.Models.HomeBase;
import com.pkasemer.worshipcenterdowntown.Models.RadioScreen;
import com.pkasemer.worshipcenterdowntown.Models.SelectedEventPage;
import com.pkasemer.worshipcenterdowntown.Models.SelectedSermon;
import com.pkasemer.worshipcenterdowntown.Models.SelectedSermonPage;
import com.pkasemer.worshipcenterdowntown.Models.SermonPage;


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

    @GET("radios.php")
    Call<RadioScreen> getRadioScreen(
            @Query("page") int pageIndex
    );

    @GET("sermons.php")
    Call<SermonPage> getSermonPageRequest(
            @Query("page") int pageIndex
    );

    @GET("selected_sermon.php")
    Call<SelectedSermonPage> getSermonDetails(
            @Query("sermonID") int sermonID,
            @Query("page") int pageIndex
    );

    @GET("selected_event.php")
    Call<SelectedEventPage> getEventDetails(
            @Query("eventID") int eventID,
            @Query("page") int pageIndex
    );

    @GET("notices.php")
    Call<AlertsPage> getAlertPageRequest(
            @Query("page") int pageIndex
    );



}