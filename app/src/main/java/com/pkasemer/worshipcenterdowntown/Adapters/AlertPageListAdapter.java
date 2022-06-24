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
import android.widget.RelativeLayout;
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
import com.pkasemer.worshipcenterdowntown.Models.Notice;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.SelectedSermonDetail;
import com.pkasemer.worshipcenterdowntown.Utils.GlideApp;

import org.jsoup.Jsoup;

import java.util.List;

public class AlertPageListAdapter extends RecyclerView.Adapter<AlertPageListAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView home_sermon_title,home_sermon_description,sermon_author,sermon_verse;
        private final RelativeLayout Redlive;


        public ItemViewHolder(View itemView) {
            super(itemView);
            home_sermon_title = itemView.findViewById(R.id.home_sermon_title);
            home_sermon_description = itemView.findViewById(R.id.home_sermon_description);
            sermon_author = itemView.findViewById(R.id.sermon_author);
            sermon_verse = itemView.findViewById(R.id.sermon_verse);
            Redlive = itemView.findViewById(R.id.Redlive);

        }
    }

    private final Context context;
    private final List<Notice> products;
      private static final String BASE_URL_IMG = "";

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();




    public AlertPageListAdapter(Context context, List<Notice> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Notice product = products.get(position);

        holder.home_sermon_title.setText(product.getNoticetitle());
        holder.home_sermon_description.setText(html2text(product.getNoticedescription()));
        holder.sermon_verse.setText(product.getCreatedDate());

        if(position != 0){
            holder.Redlive.setVisibility(View.GONE);
        }


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