package com.example.a1412023.InterStellarBookNights;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a1412023.InterStellarBookNights.R;

public class ShelfFragment extends Fragment {

    public ShelfFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        return view;
    }
}
