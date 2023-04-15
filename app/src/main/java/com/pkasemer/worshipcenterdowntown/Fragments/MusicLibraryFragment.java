package com.pkasemer.worshipcenterdowntown.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pkasemer.worshipcenterdowntown.Adapters.HomeFeedAdapter;
import com.pkasemer.worshipcenterdowntown.Adapters.RadioFeedAdapter;
import com.pkasemer.worshipcenterdowntown.Apis.ApiBase;
import com.pkasemer.worshipcenterdowntown.Apis.ApiService;
import com.pkasemer.worshipcenterdowntown.Models.HomeBase;
import com.pkasemer.worshipcenterdowntown.Models.HomeFeed;
import com.pkasemer.worshipcenterdowntown.Models.RadioPage;
import com.pkasemer.worshipcenterdowntown.Models.RadioScreen;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.RootActivity;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationAdapterCallback;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationScrollListener;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MusicLibraryFragment extends Fragment implements PaginationAdapterCallback {



    private static final String TAG = "MainActivity";

    RadioFeedAdapter radioFeedAdapter;
    LinearLayoutManager linearLayoutManager;

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

    List<RadioPage> radioPageList;

    private ApiService apiService;
    private Object PaginationAdapterCallback;


    public MusicLibraryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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

        radioFeedAdapter = new RadioFeedAdapter(getContext(), this);

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


        return view;
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
                if(radioPageList.isEmpty()){
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

}