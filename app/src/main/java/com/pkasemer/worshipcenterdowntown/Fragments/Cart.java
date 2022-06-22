package com.pkasemer.worshipcenterdowntown.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.worshipcenterdowntown.R;


public class Cart extends Fragment {


    private ProgressBar progressBar;
    RecyclerView recyclerView;

    TextView grandtotalvalue;
    LinearLayout procceed_checkout_layout,notFound_layout;

    Button btnCheckout;


    public Cart() {
        // Required empty public constructor
    }


    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        progressBar = view.findViewById(R.id.cart_main_progress);
        grandtotalvalue = view.findViewById(R.id.grandtotalvalue);
        recyclerView = view.findViewById(R.id.cart_main_recycler);
        procceed_checkout_layout = view.findViewById(R.id.procceed_checkout_layout);
        notFound_layout = view.findViewById(R.id.notFound_layout);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);



        return view;
    }









}