package com.pkasemer.worshipcenterdowntown.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pkasemer.worshipcenterdowntown.LoginMaterial;
import com.pkasemer.worshipcenterdowntown.Models.User;

/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "worshipcenterDT";
    private static final String KEY_FNAME = "key_fname";
    private static final String KEY_LNAME = "key_lname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ADDRESS = "keyaddress";
    private static final String KEY_ID = "keyid";

    // CurrentTrack
    private static final String SHARED_PREF_CURRENT_TRACK = "current_track";
    private static final String KEY_TRACK_ID = "keyTrackID";
    private static final String KEY_TRACK_TITLE = "keyTrackTitle";
    private static final String KEY_TRACK_ARTIST = "keyTrackArtist";
    private static final String KEY_TRACK_ARTIST_ID = "KeyTrackArtistID";
    private static final String KEY_TRACK_ARTWORK_URL = "KeyTrackArtworkUrl";
    private static final String KEY_TRACK_PATH = "KeyTrackPath";


    // current mediaitems
    private static final String SHARED_PREF_MEDIA_ITEMS = "current_media_items";
    private static final String KEY_MEDIA_ITEM_ID = "key_media_item_id";



    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userAccount(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FNAME, user.getFname());
        editor.putString(KEY_LNAME, user.getLname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.apply();
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FNAME, user.getFname());
        editor.putString(KEY_LNAME, user.getLname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, null) != null;
    }

//    public boolean isLoggedIn() {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_USERNAME, null) != null;
//    }


    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FNAME, null),
                sharedPreferences.getString(KEY_LNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_ADDRESS, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginMaterial.class));
    }

    public void currentTrack(int id, String title, String artist, String artistId, String artwork, String path){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_CURRENT_TRACK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TRACK_ID, id);
        editor.putString(KEY_TRACK_TITLE, title);
        editor.putString(KEY_TRACK_ARTIST, artist);
        editor.putString(KEY_TRACK_ARTIST_ID, artistId);
        editor.putString(KEY_TRACK_ARTWORK_URL, artwork);
        editor.putString(KEY_TRACK_PATH, path);
        editor.apply();
    }
}
