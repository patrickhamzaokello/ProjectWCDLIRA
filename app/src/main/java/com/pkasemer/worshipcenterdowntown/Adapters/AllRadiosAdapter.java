package com.pkasemer.worshipcenterdowntown.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.worshipcenterdowntown.Models.HomeSermon;
import com.pkasemer.worshipcenterdowntown.Models.Radio;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.SelectedSermonDetail;
import com.pkasemer.worshipcenterdowntown.Utils.GlideApp;
import com.pkasemer.worshipcenterdowntown.Utils.PlayRadioCallback;

import org.jsoup.Jsoup;

import java.util.List;

public class AllRadiosAdapter extends RecyclerView.Adapter<AllRadiosAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView title_text, category_text;
        private final ProgressBar mProgress;
        private final ImageView cover_image;


        public ItemViewHolder(View itemView) {
            super(itemView);
            cover_image = itemView.findViewById(R.id.cover_image);
            title_text = itemView.findViewById(R.id.title_text);
            category_text = itemView.findViewById(R.id.category_text);
            mProgress = itemView.findViewById(R.id.sermon_image_progress_bar);


        }
    }

    private final Context context;
    private final List<Radio> radioList;
    private static final String BASE_URL_IMG = "";
    PlayRadioCallback playRadioCallback;
    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


    public AllRadiosAdapter(Context context, List<Radio> radios, PlayRadioCallback playRadioCallback) {
        this.context = context;
        this.radioList = radios;
        this.playRadioCallback = playRadioCallback;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_station, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Radio radio = radioList.get(position);

        holder.title_text.setText(radio.getTitle());

        holder.category_text.setText(html2text(radio.getSummary()));

        Glide
                .with(context)
                .load(BASE_URL_IMG + radio.getCover())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgress.setVisibility(View.VISIBLE);
                        holder.cover_image.setImageDrawable(context.getDrawable(R.drawable.default_radio));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mProgress.setVisibility(View.GONE);
                        return false;
                    }

                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .transition(withCrossFade(factory))
                .into(holder.cover_image);


        //show toast on click of show all button
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRadioCallback.openDetails(radio);
            }
        });
    }


    private RequestBuilder<Drawable> loadImage(@NonNull String posterPath) {
        return GlideApp
                .with(context)
                .load(BASE_URL_IMG + posterPath)
                .centerCrop();
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }


    @Override
    public int getItemCount() {
        return radioList.size();
    }


}