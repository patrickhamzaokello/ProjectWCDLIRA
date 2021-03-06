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
import com.pkasemer.worshipcenterdowntown.SelectedSermonDetail;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.Utils.GlideApp;

import org.jsoup.Jsoup;

import java.util.List;

public class HomeSermonsAdapter extends RecyclerView.Adapter<HomeSermonsAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView home_sermon_title, home_sermon_description, sermon_author, sermon_verse;
        private final ProgressBar mProgress;
        private final ImageView sermon_banner;


        public ItemViewHolder(View itemView) {
            super(itemView);
            sermon_banner = itemView.findViewById(R.id.sermon_banner);
            home_sermon_title = itemView.findViewById(R.id.home_sermon_title);
            home_sermon_description = itemView.findViewById(R.id.home_sermon_description);
            sermon_author = itemView.findViewById(R.id.sermon_author);
            sermon_verse = itemView.findViewById(R.id.sermon_verse);
            mProgress = itemView.findViewById(R.id.sermon_image_progress_bar);


        }
    }

    private final Context context;
    private final List<HomeSermon> products;
    private static final String BASE_URL_IMG = "";

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


    public HomeSermonsAdapter(Context context, List<HomeSermon> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sermon_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final HomeSermon product = products.get(position);

        holder.home_sermon_title.setText(product.getSermontitle());

        holder.home_sermon_description.setText(html2text(product.getSermondescription()));
        holder.sermon_author.setText(product.getSermonauthor());
        holder.sermon_verse.setText(product.getSermondate());

        Glide
                .with(context)
                .load(BASE_URL_IMG + product.getSermonbanner())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgress.setVisibility(View.VISIBLE);
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
                .into(holder.sermon_banner);


        //show toast on click of show all button
        holder.sermon_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), SelectedSermonDetail.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                //PACK DATA
                i.putExtra("SENDER_KEY", "MenuDetails");
                i.putExtra("selectedSermonId", product.getSermonid());
                i.putExtra("selectedSermonTitle", product.getSermontitle());
                context.startActivity(i);
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
        return products.size();
    }


}