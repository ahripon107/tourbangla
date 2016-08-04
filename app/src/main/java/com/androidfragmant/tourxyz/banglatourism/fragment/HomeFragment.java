package com.androidfragmant.tourxyz.banglatourism.fragment;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.HomeFragmentRecyclerAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentElement;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;

import java.util.ArrayList;

/**
 * Created by Ripon on 6/11/15.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<HomeFragmentElement> elements;

    public HomeFragment() {
        elements = new ArrayList<>();
        elements.add(new HomeFragmentElement("Explore amazing tourist spots of beautiful Bangladesh!","Explore Places"));
        elements.add(new HomeFragmentElement("Explore offers from tour operators and hotels!","Tour offers"));
        elements.add(new HomeFragmentElement("Want to share your and read other's tour experience? Explore Tour Blog!","Tour Blog"));
        elements.add(new HomeFragmentElement("Want to know anything about a place? Ask in Forum!","Forum"));
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
