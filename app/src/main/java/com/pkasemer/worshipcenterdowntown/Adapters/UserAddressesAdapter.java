package com.pkasemer.worshipcenterdowntown.Adapters;

import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.worshipcenterdowntown.Models.UserAddress;
import com.pkasemer.worshipcenterdowntown.PlaceOrder;
import com.pkasemer.worshipcenterdowntown.R;
import com.pkasemer.worshipcenterdowntown.RootActivity;
import com.pkasemer.worshipcenterdowntown.Utils.PaginationAdapterCallback;
import com.pkasemer.worshipcenterdowntown.localDatabase.SenseDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class UserAddressesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    private List<UserAddress> userAddresses;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;


    private final PaginationAdapterCallback mCallback;
    private String errorMsg;

    public UserAddressesAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        userAddresses = new ArrayList<>();
    }

    SenseDBHelper db;
    boolean address_itemchecker;

    public List<UserAddress> getMovies() {
        return userAddresses;
    }

    public void setMovies(List<UserAddress> categories) {
        this.userAddresses = categories;
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
        View v1 = inflater.inflate(R.layout.addresslayout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        UserAddress userAddress = userAddresses.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                if (userAddress != null) {

                    db = new SenseDBHelper(context);

                    address_itemchecker = db.checkAddressinDB(String.valueOf(userAddress.getId()));

                    if (address_itemchecker) {

                        movieVH.addressCard.setBackgroundColor(context.getResources().getColor(R.color.white));


                    } else {

                        movieVH.addressCard.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

                    }

                    movieVH.username.setText(userAddress.getUsername());
                    movieVH.email.setText(userAddress.getEmail());
                    movieVH.phone.setText(userAddress.getPhone());
                    movieVH.city.setText(userAddress.getCountry() + " , " + userAddress.getCity() + " , " + userAddress.getAddress());

                    movieVH.addressCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            movieVH.addressCard.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

                            address_itemchecker = db.checkAddressinDB(String.valueOf(userAddress.getId()));


                            if (address_itemchecker) {
                                db.clearAddress();
                                db.addADDress(userAddress.getId());
                                movieVH.addressCard.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
                                Toast.makeText(view.getContext(), "Address Added ", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(context.getApplicationContext(), PlaceOrder.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                //PACK DATA
                                i.putExtra("SELECTED_ADDRESS", "PlaceOrder");
                                i.putExtra("selected_address_id", userAddress.getId());
                                context.startActivity(i);


                            } else {
                                db.deleteAddress(String.valueOf(userAddress.getId()));
                                movieVH.addressCard.setBackgroundColor(context.getResources().getColor(R.color.white));
                                Toast.makeText(view.getContext(), "Address Deleted", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                } else {
                    mCallback.requestfailed();
                }
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
        return userAddresses == null ? 0 : userAddresses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == userAddresses.size() - 1 && isLoadingAdded) ?
                LOADING : ITEM;
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


    public void add(UserAddress r) {
        userAddresses.add(r);
        notifyItemInserted(userAddresses.size() - 1);
    }

    public void addAll(List<UserAddress> userAddresses) {
        for (UserAddress userAddress : userAddresses) {
            add(userAddress);
        }
    }

    public void remove(UserAddress r) {
        int position = userAddresses.indexOf(r);
        if (position > -1) {
            userAddresses.remove(position);
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
        add(new UserAddress());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = userAddresses.size() - 1;
        UserAddress userAddress = getItem(position);

        if (userAddress != null) {
            userAddresses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(userAddresses.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public UserAddress getItem(int position) {
        return userAddresses.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView username, email, phone, city;
        private CardView addressCard;

        public MovieVH(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            email = (TextView) itemView.findViewById(R.id.email);
            phone = (TextView) itemView.findViewById(R.id.phone);
            city = (TextView) itemView.findViewById(R.id.city);
            addressCard = (CardView) itemView.findViewById(R.id.addressCard);


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