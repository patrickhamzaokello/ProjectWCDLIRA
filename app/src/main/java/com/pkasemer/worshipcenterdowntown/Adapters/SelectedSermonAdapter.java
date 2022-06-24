package com.pkasemer.worshipcenterdowntown.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import com.pkasemer.worshipcenterdowntown.Models.SelectedSermon;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.RootActivity;
import com.pkasemer.worshipcenterdowntown.Utils.GlideApp;
import com.pkasemer.worshipcenterdowntown.Utils.MenuDetailListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class SelectedSermonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;






    private List<SelectedSermon> selectedProducts;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private final MenuDetailListener mCallback;
    private String errorMsg;


    int minteger = 1;
    int totalPrice;


    public SelectedSermonAdapter(Context context, MenuDetailListener callback) {
        this.context = context;
        this.mCallback = callback;
        selectedProducts = new ArrayList<>();
    }

    public List<SelectedSermon> getMovies() {
        return selectedProducts;
    }

    public void setMovies(List<SelectedSermon> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HERO:
                View viewHero = inflater.inflate(R.layout.selected_sermon_hero, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.pagination_item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.sermon_detail_similar_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SelectedSermon selectedProduct = selectedProducts.get(position); // Movie

        switch (getItemViewType(position)) {
            case HERO:
                final HeroVH heroVh = (HeroVH) holder;


                heroVh.sermon_title.setText(selectedProduct.getSermontitle());
                heroVh.sermon_description.setText(Html.fromHtml(selectedProduct.getSermondescription()));
                heroVh.sermon_preacher.setText(selectedProduct.getSermonauthor());
                heroVh.sermon_info_date.setText(selectedProduct.getCdate());

                heroVh.sermon_description.setMovementMethod(LinkMovementMethod.getInstance());

                Glide
                        .with(context)
                        .load(selectedProduct.getSermonbanner())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                heroVh.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                heroVh.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .transition(withCrossFade(factory))
                        .into(heroVh.selected_sermon_banner);


                break;
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                //recycler view for grid items products

                //recycler view for items
                /* set layout manager on basis of recyclerview enum type */

                LinearLayoutManager ITEMVH_HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                movieVH.itemRecyclerView.setLayoutManager(ITEMVH_HorizontalLayout);


                SimiliarSermonsAdapter adapter = new SimiliarSermonsAdapter(context, selectedProduct.getHomeSermons());
                movieVH.itemRecyclerView.setAdapter(adapter);

                break;

            case LOADING:
//                Do nothing
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return selectedProducts == null ? 0 : selectedProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HERO;
        } else {
            return (position == selectedProducts.size() - 1 && isLoadingAdded) ?
                    LOADING : ITEM;
        }
    }

    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof RootActivity) {
            RootActivity mainActivity = (RootActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag, "MenuDetails");
        }

    }




    /*
   Helpers
   _________________________________________________________________________________________________
    */


    private RequestBuilder<Drawable> loadImage(@NonNull String posterPath) {
        return GlideApp
                .with(context)
                .load(posterPath)
                .centerCrop();
    }

    public void add(SelectedSermon r) {
        selectedProducts.add(r);
        notifyItemInserted(selectedProducts.size() - 1);
    }

    public void addAll(List<SelectedSermon> selectedProducts) {
        for (SelectedSermon selectedProduct : selectedProducts) {
            add(selectedProduct);
        }
    }

    public void remove(SelectedSermon r) {
        int position = selectedProducts.indexOf(r);
        if (position > -1) {
            selectedProducts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new SelectedSermon());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = selectedProducts.size() - 1;
        SelectedSermon selectedProduct = getItem(position);

        if (selectedProduct != null) {
            selectedProducts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(selectedProducts.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public SelectedSermon getItem(int position) {
        return selectedProducts.get(position);
    }








   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */

    protected class HeroVH extends RecyclerView.ViewHolder {
        private final ImageView selected_sermon_banner;
        private final TextView sermon_title, sermon_description, sermon_preacher, sermon_info_date;


        private final ProgressBar mProgress;

        private final LinearLayout relatedProductslayout;


        public HeroVH(View itemView) {
            super(itemView);
            // init views
            selected_sermon_banner = itemView.findViewById(R.id.selected_sermon_banner);
            sermon_title = itemView.findViewById(R.id.sermon_title);
            sermon_description = itemView.findViewById(R.id.sermon_description);


            sermon_preacher = itemView.findViewById(R.id.sermon_preacher);
            sermon_info_date = itemView.findViewById(R.id.sermon_info_date);

            relatedProductslayout = itemView.findViewById(R.id.relatedProductslayout);


            mProgress = itemView.findViewById(R.id.image_progress);


        }
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView sectionLabel, showAllButton;
        private RecyclerView itemRecyclerView;

        public MovieVH(View itemView) {
            super(itemView);

            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            showAllButton = (TextView) itemView.findViewById(R.id.section_show_all_button);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProgressBar mProgressBar;
        private final ImageButton mRetryBtn;
        private final TextView mErrorTxt;
        private final LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }


}