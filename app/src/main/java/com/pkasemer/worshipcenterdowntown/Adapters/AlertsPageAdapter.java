package com.pkasemer.worshipcenterdowntown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.worshipcenterdowntown.Models.AllNotice;
import com.pkasemer.worshipcenterdowntown.Models.AllSermon;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class AlertsPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    //    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";
    private static final String BASE_URL_IMG = "";


    private List<AllNotice> homeFeeds;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;


    private final PaginationAdapterCallback mCallback;
    private String errorMsg;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public AlertsPageAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        homeFeeds = new ArrayList<>();
    }

    public List<AllNotice> getMovies() {
        return homeFeeds;
    }

    public void setMovies(List<AllNotice> homeFeeds) {
        this.homeFeeds = homeFeeds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

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
        View v1 = inflater.inflate(R.layout.alert_page_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AllNotice homeFeed = homeFeeds.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                movieVH.sectionLabel.setText(homeFeed.getHeading());
                movieVH.section_label_desc.setText(homeFeed.getLabel());

                /* set layout manager on basis of recyclerview enum type */
                LinearLayoutManager ITEMVH_HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                movieVH.itemRecyclerView.setLayoutManager(ITEMVH_HorizontalLayout);

                AlertPageListAdapter adapter = new AlertPageListAdapter(context, homeFeed.getNotice());
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
        return homeFeeds == null ? 0 : homeFeeds.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == homeFeeds.size() - 1 && isLoadingAdded) ?
                LOADING : ITEM;
    }







    /*
   Helpers
   _________________________________________________________________________________________________
    */


    public void add(AllNotice r) {
        homeFeeds.add(r);
        notifyItemInserted(homeFeeds.size() - 1);
    }

    public void addAll(List<AllNotice> moveSelectedCategoryMenuItemResults) {
        for (AllNotice selectedCategoryMenuItemResult : moveSelectedCategoryMenuItemResults) {
            add(selectedCategoryMenuItemResult);
        }
    }

    public void remove(AllNotice r) {
        int position = homeFeeds.indexOf(r);
        if (position > -1) {
            homeFeeds.remove(position);
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
        add(new AllNotice());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = homeFeeds.size() - 1;
        AllNotice selectedCategoryMenuItemResult = getItem(position);

        if (selectedCategoryMenuItemResult != null) {
            homeFeeds.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(homeFeeds.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public AllNotice getItem(int position) {
        return homeFeeds.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView sectionLabel, section_label_desc;
        private RecyclerView itemRecyclerView;

        public MovieVH(View itemView) {
            super(itemView);

            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            section_label_desc = (TextView) itemView.findViewById(R.id.section_label_desc);
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