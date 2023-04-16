package com.pkasemer.worshipcenterdowntown.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.pkasemer.worshipcenterdowntown.Adapters.RadioFeedAdapter;
import com.pkasemer.worshipcenterdowntown.Apis.ApiBase;
import com.pkasemer.worshipcenterdowntown.Apis.ApiService;
import com.pkasemer.worshipcenterdowntown.Dialogs.ShareFeature;
import com.pkasemer.worshipcenterdowntown.Models.Radio;
import com.pkasemer.worshipcenterdowntown.Models.RadioPage;
import com.pkasemer.worshipcenterdowntown.Models.RadioScreen;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.RootActivity;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationAdapterCallback;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationScrollListener;
import com.pkasemer.worshipcenterdowntown.Utils.PlayRadioCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MusicLibraryFragment extends Fragment implements PaginationAdapterCallback, PlayRadioCallback {


    private static final String TAG = "MainActivity";

    RadioFeedAdapter radioFeedAdapter;
    LinearLayoutManager linearLayoutManager;
    private Dialog track_dialog;
    private ImageButton linkBtn, whatsappBtn, twitterBtn, sharemoreBtn, closebtn;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private final int selectCategoryId = 3;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
    List<RadioPage> radioPageList;

    private ApiService apiService;
    private Object PaginationAdapterCallback;

    ExoPlayer player;
    private RootActivity rootActivity;

    public MusicLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootActivity = (RootActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rv = view.findViewById(R.id.main_recycler);
        progressBar = view.findViewById(R.id.main_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = view.findViewById(R.id.main_swiperefresh);

        radioFeedAdapter = new RadioFeedAdapter(getContext(), this, this, rootActivity);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(radioFeedAdapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        apiService = ApiBase.getClient(getContext()).create(ApiService.class);


        btnRetry.setOnClickListener(v -> loadFirstPage());

        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

        shareTrackDialog();
        return view;
    }

    private void shareTrackDialog() {
        track_dialog = new Dialog(getContext());
        track_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        track_dialog.setContentView(R.layout.fragment_current_track_share_dialog);
        linkBtn = track_dialog.findViewById(R.id.link);
        ConstraintLayout dialog_background = track_dialog.findViewById(R.id.dialog_background);
        whatsappBtn = track_dialog.findViewById(R.id.whatsapp);
        twitterBtn = track_dialog.findViewById(R.id.twitter);
        sharemoreBtn = track_dialog.findViewById(R.id.Sharemore);
        closebtn = track_dialog.findViewById(R.id.closebtn);


    }

    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callHomeCategories().isExecuted())
            callHomeCategories().cancel();

        // TODO: Check if data is stale.
        //  Execute network request if cache is expired; otherwise do not update data.
        radioFeedAdapter.getMovies().clear();
        radioFeedAdapter.notifyDataSetChanged();
        loadFirstPage();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callHomeCategories().enqueue(new Callback<RadioScreen>() {
            @Override
            public void onResponse(Call<RadioScreen> call, Response<RadioScreen> response) {
                hideErrorView();

//                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                radioPageList = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                if (radioPageList.isEmpty()) {
                    showCategoryErrorView();
                    return;
                } else {
                    radioFeedAdapter.addAll(radioPageList);
                }

                if (currentPage < TOTAL_PAGES) radioFeedAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RadioScreen> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<RadioPage> fetchResults(Response<RadioScreen> response) {
        RadioScreen radioScreen = response.body();
        TOTAL_PAGES = radioScreen.getTotalPages();
        System.out.println("total pages" + TOTAL_PAGES);

        return radioScreen.getRadioPage();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callHomeCategories().enqueue(new Callback<RadioScreen>() {
            @Override
            public void onResponse(Call<RadioScreen> call, Response<RadioScreen> response) {
                Log.i(TAG, "onResponse: " + currentPage
                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                radioFeedAdapter.removeLoadingFooter();
                isLoading = false;

                radioPageList = fetchResults(response);
                radioFeedAdapter.addAll(radioPageList);

                if (currentPage != TOTAL_PAGES) radioFeedAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<RadioScreen> call, Throwable t) {
                t.printStackTrace();
                radioFeedAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<RadioScreen> callHomeCategories() {
        return apiService.getRadioScreen(
                currentPage
        );
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    @Override
    public void requestfailed() {

    }

    @Override
    public void onResume() {
        super.onResume();
        doRefresh();
    }

    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showCategoryErrorView() {

        progressBar.setVisibility(View.GONE);

        AlertDialog.Builder android = new AlertDialog.Builder(getContext());
        android.setTitle("Coming Soon");
        android.setIcon(R.drawable.aboutus);
        android.setMessage("This Menu Category will be updated with great tastes soon, Stay Alert for Updates.")
                .setCancelable(false)

                .setPositiveButton("Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //go to activity
                        Intent intent = new Intent(getActivity(), RootActivity.class);
                        startActivity(intent);
                    }
                });
        android.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //go to activity
                Intent intent = new Intent(getActivity(), RootActivity.class);
                startActivity(intent);
            }
        });
        android.create().show();

    }


    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void playablbum(Radio radio) {

        if (radio != null) {
            //start the player service
            player = rootActivity.player;
            Log.d("mcallback", String.valueOf(player));
            player.setMediaItems(getMediaItems(radio));
            //prepare and play the song
            player.prepare();
            player.play();
        } else {
            Toast.makeText(getContext(), "Unable to Play Track", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openDetails(Radio Radio) {
        dialogParent(Radio);
    }

    @Override
    public void shareFeatureCallback(String share_id, String name, String artist, String share_type) {
        ShareFeature shareFeature = new ShareFeature(share_id, name, artist, share_type);
        shareFeature.setCancelable(true);
        shareFeature.show(getChildFragmentManager(), "ShareTrackGeneral");
    }

    private List<MediaItem> getMediaItems(Radio radio) {
        List<MediaItem> mediaItems = new ArrayList<>();

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(radio.getPath())
                .setMediaMetadata(getMetadata(radio))
                .build();

        // add the media items to media items list
        mediaItems.add(mediaItem);
        return mediaItems;
    }

    private MediaMetadata getMetadata(Radio radio) {
        Bundle bundle = new Bundle();
        bundle.putString("artist_bn_id", radio.getId());
        bundle.putString("song_bn_id", radio.getId());
        bundle.putString("song_path", radio.getPath());

        return new MediaMetadata.Builder()
                .setTitle(radio.getTitle())
                .setArtist("WCDT Radio")
                .setArtworkUri(Uri.parse(radio.getCover()))
                .setExtras(bundle)
                .build();
    }


    private void dialogParent(Radio Radio) {

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                track_dialog.dismiss();
            }
        });

//        Glide.with(getContext())
//                .applyDefaultRequestOptions(new RequestOptions()
//                        .placeholder(R.drawable.default_radio)
//                        .error(R.drawable.default_radio))
//                .load(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artworkUri)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)    // cache both original & resized image
//                .centerCrop()
//                .transition(withCrossFade(factory))
//                .into(shareArtwork);


        track_dialog.show();
        track_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        track_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        track_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        track_dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}