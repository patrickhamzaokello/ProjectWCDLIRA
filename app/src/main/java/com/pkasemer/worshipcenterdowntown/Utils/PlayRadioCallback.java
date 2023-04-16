package com.pkasemer.worshipcenterdowntown.Utils;


import com.pkasemer.worshipcenterdowntown.Models.Radio;

import java.util.List;

/**
 * Created by Suleiman on 16/11/16.
 */

public interface PlayRadioCallback {
    void playablbum(Radio radio);

    void openDetails(Radio Radio);
    void shareFeatureCallback(String share_id,String name, String artist, String share_type);
}