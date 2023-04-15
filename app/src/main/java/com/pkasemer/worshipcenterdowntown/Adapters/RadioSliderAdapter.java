package com.pkasemer.worshipcenterdowntown.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.worshipcenterdowntown.Models.Radio;
import com.pkasemer.worshipcenterdowntown.Models.SliderBanner;
import com.pkasemer.worshipcenterdowntown.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class RadioSliderAdapter extends SliderViewAdapter<RadioSliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<Radio> sliderBanners;
    private final Context context;
    private static final String BASE_URL_IMG = "";

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    // Constructor
    public RadioSliderAdapter(Context context, List<Radio> radioBanners) {
        this.context = context;
        this.sliderBanners = radioBanners;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_slider_design, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final Radio sliderBanner = sliderBanners.get(position);

        Glide
                .with(viewHolder.itemView)
                .load(BASE_URL_IMG + sliderBanner.getCover())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.msliderProgress.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.msliderProgress.setVisibility(View.GONE);
                        return false;
                    }

                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .transition(withCrossFade(factory))
                .into(viewHolder.imageViewBackground);
    }


    @Override
    public int getCount() {
        return sliderBanners.size();
    }

    static class SliderAdapterViewHolder extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ProgressBar msliderProgress;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            msliderProgress = itemView.findViewById(R.id.msliderProgress);
            this.itemView = itemView;
        }
    }
}