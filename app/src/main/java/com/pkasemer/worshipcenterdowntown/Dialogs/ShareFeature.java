package com.pkasemer.worshipcenterdowntown.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pkasemer.worshipcenterdowntown.R;


public class ShareFeature extends DialogFragment {



    public ImageButton Share_more_btn,whatsapp_btn,twitter_btn,closebtn;
    private final String shareID;
    private final String name;
    private final String artist;
    private final String shareTYPE;
    private final String whatsappPackage = "com.whatsapp";
    private final String twitterPackage = "com.twitter.android";

    public ShareFeature(String share_id, String name, String artist, String share_type) {
        this.shareID = share_id;
        this.shareTYPE = share_type;
        this.name = name;
        this.artist = artist;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_share_feature, null);

        Share_more_btn = view.findViewById(R.id.Sharemore);
        whatsapp_btn = view.findViewById(R.id.whatsapp);
        twitter_btn = view.findViewById(R.id.twitter);
        closebtn = view.findViewById(R.id.closebtn);
        builder.setView(view);

        // Add action buttons
        whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentStarter(whatsappPackage,linkPicker(shareTYPE,name, artist,shareID));
            }
        });
        twitter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentStarter(twitterPackage,linkPicker(shareTYPE,name, artist,shareID));
            }
        });
        // Add action buttons
        Share_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentStarter(null,linkPicker(shareTYPE,name, artist,shareID));
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    void intentStarter(String package_name, String url_final_link){
        // Create a URI for the track
        Uri trackUri = Uri.parse(url_final_link);
        // Create an intent for sharing the track
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String intentType = "text/plain";
        shareIntent.setType(intentType);
        shareIntent.putExtra(Intent.EXTRA_TEXT, trackUri.toString());
        if(package_name != null){
            shareIntent.setPackage(package_name);
        }
        // Display a list of available sharing options
        String intentHeading = "Share Feature";
        startActivity(Intent.createChooser(shareIntent, intentHeading));
    }

    public String linkPicker(String url_share_type, String title, String artistname, String url_share_id){
        String web_link = " https://open.mwonya.com/";
        if(url_share_type.equalsIgnoreCase(this.getString(R.string.share_type_artist))){
            String social = title +" is on Mwonya. Join the fan club & experience great content from "+title+". Click the link for more details! ";
            web_link = social+ " https://open.mwonya.com/link/share/artist/"+ url_share_id;
        }
        if(url_share_type.equalsIgnoreCase(this.getString(R.string.share_type_collection))){
            String social = "Listen to "+ title +" by "+artistname+" on Mwonya. ";
            web_link = social+ " https://open.mwonya.com/link/share/collection/"+ url_share_id;
        }
        if(url_share_type.equalsIgnoreCase(this.getString(R.string.share_type_track))){
            String social = "Listen to "+title+" by "+artistname+" on Mwonya by clicking the link below   ";
            web_link = social+ " https://open.mwonya.com/link/share/track/"+ url_share_id;
        }
        if (url_share_type.equalsIgnoreCase(this.getString(R.string.share_type_playlist))) {
            String social = "Here is a playlist made just for you. Listen to the "+ title +" playlist, on Mwonya. ";
            web_link = social+ " https://open.mwonya.com/link/share/playlist/" + url_share_id;
        }
        if (url_share_type.equalsIgnoreCase(this.getString(R.string.share_type_event))) {
            String social = "Join us for  "+ title +" happening on "+artistname+". Don't miss out on this exciting experience. Follow the link to get all the details! ";
            web_link = social+ "https://open.mwonya.com/link/share/event/" + url_share_id;
        }
        return web_link;
    }


}