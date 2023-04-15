package com.pkasemer.worshipcenterdowntown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.worshipcenterdowntown.HelperClasses.SharedPrefManager;
import com.pkasemer.worshipcenterdowntown.Models.RadioPage;
import com.pkasemer.worshipcenterdowntown.Models.User;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.RootActivity;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationAdapterCallback;
import com.pkasemer.worshipcenterdowntown.Utils.PlayRadioCallback;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class RadioFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 3;
    private static final int LOADING = 4;
    private static final int HERO = 0;
    private static final int DAILY_HIGHLIGHTS = 1;
    private static final int DAILY_EVENTS = 2;

    //    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";
    private static final String BASE_URL_IMG = "";


    private List<RadioPage> radioPages;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;


    private final PaginationAdapterCallback mCallback;
    private String errorMsg;
    private RootActivity rootActivity;
    private final PlayRadioCallback playRadioCallback;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public RadioFeedAdapter(Context context, PaginationAdapterCallback callback,PlayRadioCallback playRadioCallback,RootActivity rootActivity) {
        this.context = context;
        this.mCallback = callback;
        this.playRadioCallback = playRadioCallback;
        this.rootActivity = rootActivity;
        radioPages = new ArrayList<>();
    }

    public List<RadioPage> getMovies() {
        return radioPages;
    }

    public void setMovies(List<RadioPage> radioPageList) {
        this.radioPages = radioPageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HERO:
                View viewHero = inflater.inflate(R.layout.radio_page_slider, parent, false);
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
        View v1 = inflater.inflate(R.layout.all_radio_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RadioPage radioPage = radioPages.get(position); // Movie

        switch (getItemViewType(position)) {
            case HERO:
                final HeroVH heroVh = (HeroVH) holder;
                heroVh.sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                heroVh.sliderView.setScrollTimeInSec(3);
                heroVh.sliderView.setAutoCycle(true);
                heroVh.sliderView.startAutoCycle();
                // passing this array list inside our adapter class.
                RadioSliderAdapter radioSliderAdapter = new RadioSliderAdapter(context, radioPage.getRadio(),playRadioCallback);
                heroVh.sliderView.setSliderAdapter(radioSliderAdapter);
                //getting the current user
                User user = SharedPrefManager.getInstance(context).getUser();
                String userFname = user.getFname();

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                    heroVh.home_greeting.setText("Good morning, " + userFname);
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    heroVh.home_greeting.setText("Good afternoon, " + userFname);
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                    heroVh.home_greeting.setText("Good evening, " + userFname);
                } else if (timeOfDay >= 21 && timeOfDay <= 24) {
                    heroVh.home_greeting.setText("Good night, " + userFname);
                }



                break;

            case ITEM:
                final MovieVH radioVH = (MovieVH) holder;
                radioVH.sectionLabel.setText(radioPage.getHeading());
                radioVH.section_label_desc.setText(radioPage.getLabel());
                /* set layout manager on basis of recyclerview enum type */
                // set up grid layout manager
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2);

// set up recycler view
                radioVH.itemRecyclerView.setLayoutManager(layoutManager);
                AllRadiosAdapter adapter = new AllRadiosAdapter(context, radioPage.getRadio(),playRadioCallback);
                radioVH.itemRecyclerView.setAdapter(adapter);
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
        return radioPages == null ? 0 : radioPages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HERO;
        } else {
            return (position == radioPages.size() - 1 && isLoadingAdded) ?
                    LOADING : ITEM;
        }
    }







    /*
   Helpers
   _________________________________________________________________________________________________
    */


    public void add(RadioPage r) {
        radioPages.add(r);
        notifyItemInserted(radioPages.size() - 1);
    }

    public void addAll(List<RadioPage> radioPageList) {
        for (RadioPage radioPage : radioPageList) {
            add(radioPage);
        }
    }

    public void remove(RadioPage r) {
        int position = radioPages.indexOf(r);
        if (position > -1) {
            radioPages.remove(position);
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
        add(new RadioPage());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = radioPages.size() - 1;
        RadioPage radioPage = getItem(position);

        if (radioPage != null) {
            radioPages.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(radioPages.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public RadioPage getItem(int position) {
        return radioPages.get(position);
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