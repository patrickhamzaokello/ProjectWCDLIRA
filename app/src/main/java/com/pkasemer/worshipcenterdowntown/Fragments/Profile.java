package com.pkasemer.worshipcenterdowntown.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.worshipcenterdowntown.R;


public class Profile extends Fragment {


    private ProgressBar progressBar;
    RecyclerView recyclerView;

    TextView grandtotalvalue;
    LinearLayout procceed_checkout_layout,notFound_layout;

    Button btnCheckout;


    public Profile() {
        // Required empty public constructor
    }


    public static Alert newInstance(String param1, String param2) {
        Alert fragment = new Alert();
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




        return view;
    }



}