package com.pkasemer.worshipcenterdowntown.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
import com.pkasemer.worshipcenterdowntown.Models.HomeFeed;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationAdapterCallback;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 3;
    private static final int LOADING = 4;
    private static final int HERO = 0;
    private static final int DAILY_HIGHLIGHTS = 1;
    private static final int DAILY_EVENTS = 2;

    //    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";
    private static final String BASE_URL_IMG = "";


    private List<HomeFeed> homeFeeds;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;


    private final PaginationAdapterCallback mCallback;
    private String errorMsg;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public HomeFeedAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        homeFeeds = new ArrayList<>();
    }

    public List<HomeFeed> getMovies() {
        return homeFeeds;
    }

    public void setMovies(List<HomeFeed> homeFeeds) {
        this.homeFeeds = homeFeeds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HERO:
                View viewHero = inflater.inflate(R.layout.home_slider_layout, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
            case DAILY_HIGHLIGHTS:
                View viewCategory = inflater.inflate(R.layout.home_daily_highlights_layout, parent, false);
                viewHolder = new CategoryVH(viewCategory);
                break;
            case DAILY_EVENTS:
                View viewsCategory = inflater.inflate(R.layout.home_event_layout, parent, false);
                viewHolder = new EventsVH(viewsCategory);
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
        View v1 = inflater.inflate(R.layout.home_sermon_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeFeed homeFeed = homeFeeds.get(position); // Movie

        switch (getItemViewType(position)) {
            case HERO:
                final HeroVH heroVh = (HeroVH) holder;
                heroVh.sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                heroVh.sliderView.setScrollTimeInSec(3);
                heroVh.sliderView.setAutoCycle(true);
                heroVh.sliderView.startAutoCycle();
                // passing this array list inside our adapter class.
                HomeSliderAdapter slideradapter = new HomeSliderAdapter(context, homeFeed.getSliderBanners());
                heroVh.sliderView.setSliderAdapter(slideradapter);


                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if(timeOfDay >= 0 && timeOfDay < 12){
                    heroVh.home_greeting.setText("Good morning, Okot");
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    heroVh.home_greeting.setText("Good afternoon, Okot");
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    heroVh.home_greeting.setText("Good evening, Okot");
                }else if(timeOfDay >= 22 && timeOfDay < 24){
                    heroVh.home_greeting.setText("Good night, Okot");
                }

                break;
            case DAILY_HIGHLIGHTS:
                final CategoryVH categoryVH = (CategoryVH) holder;
                HomeDailyHighlightAdapter homeDailyHighlightAdapter = new HomeDailyHighlightAdapter(context);

                LinearLayoutManager HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                categoryVH.category_recycler_view.setLayoutManager(HorizontalLayout);
                categoryVH.category_recycler_view.setAdapter(homeDailyHighlightAdapter);
                homeDailyHighlightAdapter.addAll(homeFeed.getDailyHighlights());

                break;

            case DAILY_EVENTS:
                final EventsVH eventsVH = (EventsVH) holder;
                HomeEventsAdapter homeEvents = new HomeEventsAdapter(context);
                eventsVH.section_label.setText(homeFeed.getHeading());
                eventsVH.section_label_desc.setText(homeFeed.getLabel());

                LinearLayoutManager EventsVH_HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                eventsVH.EventsVH_category_recycler_view.setLayoutManager(EventsVH_HorizontalLayout);
                eventsVH.EventsVH_category_recycler_view.setAdapter(homeEvents);
                homeEvents.addAll(homeFeed.getHomeEvents());

                break;
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                movieVH.sectionLabel.setText(homeFeed.getHeading());
                movieVH.section_label_desc.setText(homeFeed.getLabel());


                /* set layout manager on basis of recyclerview enum type */
                LinearLayoutManager ITEMVH_HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                movieVH.itemRecyclerView.setLayoutManager(ITEMVH_HorizontalLayout);


                HomeSermonsAdapter adapter = new HomeSermonsAdapter(context,homeFeed.getHomeSermons());
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
        if (position == 0) {
            return HERO;
        } else if (position == 1) {
            return DAILY_HIGHLIGHTS;
        }  else if (position == 2) {
            return DAILY_EVENTS;
        } else {
            return (position == homeFeeds.size() - 1 && isLoadingAdded) ?
                    LOADING : ITEM;
        }
    }







    /*
   Helpers
   _________________________________________________________________________________________________
    */


    public void add(HomeFeed r) {
        homeFeeds.add(r);
        notifyItemInserted(homeFeeds.size() - 1);
    }

    public void addAll(List<HomeFeed> moveSelectedCategoryMenuItemResults) {
        for (HomeFeed selectedCategoryMenuItemResult : moveSelectedCategoryMenuItemResults) {
            add(selectedCategoryMenuItemResult);
        }
    }

    public void remove(HomeFeed r) {
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
        add(new HomeFeed());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = homeFeeds.size() - 1;
        HomeFeed selectedCategoryMenuItemResult = getItem(position);

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

    public HomeFeed getItem(int position) {
        return homeFeeds.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */

    protected class HeroVH extends RecyclerView.ViewHolder {

        private final SliderView sliderView;
        private final TextView home_greeting;

        public HeroVH(View itemView) {
            super(itemView);
            // init views
            sliderView = itemView.findViewById(R.id.home_slider);
            home_greeting = itemView.findViewById(R.id.home_greeting);


        }
    }


    protected class CategoryVH extends RecyclerView.ViewHolder {

        private final RecyclerView category_recycler_view;

        public CategoryVH(View itemView) {
            super(itemView);
            // init views
            category_recycler_view = itemView.findViewById(R.id.category_recycler_view);

        }
    }


    protected class EventsVH extends RecyclerView.ViewHolder {

        private final RecyclerView EventsVH_category_recycler_view;
        private final TextView section_label, section_label_desc;

        public EventsVH(View itemView) {
            super(itemView);
            // init views
            EventsVH_category_recycler_view = itemView.findViewById(R.id.home_event_recycler_view);
            section_label = itemView.findViewById(R.id.section_label);
            section_label_desc = itemView.findViewById(R.id.section_label_desc);

        }
    }

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