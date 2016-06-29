package com.sfuronlabs.ripon.tourbangla.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.adapter.GridAdapter;
import com.sfuronlabs.ripon.tourbangla.adapter.HomeFragmentRecyclerAdapter;
import com.sfuronlabs.ripon.tourbangla.model.HomeFragmentElement;

import java.util.ArrayList;

/**
 * Created by Ripon on 6/11/15.
 */
public class HomeFragment extends Fragment {

    String web[];
    String picname[];
    RecyclerView recyclerView;
    ArrayList<HomeFragmentElement> elements;

    public HomeFragment() {
        elements = new ArrayList<>();
        elements.add(new HomeFragmentElement("Browse By Division","image","Browse By Division"));
        elements.add(new HomeFragmentElement("Browse by type","image","Browse by type"));
        elements.add(new HomeFragmentElement("Hotels","image","Hotels"));
        elements.add(new HomeFragmentElement("Tour operators","image","Tour operators"));
        elements.add(new HomeFragmentElement("Tour Blog","image","Tour Blog"));
        elements.add(new HomeFragmentElement("Suggest New Place","image","Suggest New Place"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeFragmentRecyclerAdapter adapter = new HomeFragmentRecyclerAdapter(getContext(),elements);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewselect);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
