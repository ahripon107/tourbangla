package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.NewPlaceDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.GridAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ripon on 6/11/15.
 */
public class WishListFragment extends Fragment {
    ListView listView;
    SharedPreferences sharedPreference;
    String[] web = new String[0];
    String[] picname = new String[0];
    GridAdapter gridAdapter;
    ArrayList<Place> allWishListPlaces;

    public WishListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        allWishListPlaces = new ArrayList<>();
        Map<String, ?> elements = sharedPreference.getAll();
        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            allWishListPlaces.add(place);
        }
        if (allWishListPlaces != null) {

            web = new String[allWishListPlaces.size()];
            picname = new String[allWishListPlaces.size()];

            for (int j = 0; j < allWishListPlaces.size(); j++) {
                web[j] = allWishListPlaces.get(j).getName();
                picname[j] = allWishListPlaces.get(j).getPicture();
            }
        }

        gridAdapter = new GridAdapter(getActivity(),
                web, picname);
        listView.setAdapter(gridAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        listView = (ListView) rootView.findViewById(R.id.gridviewfavourite);
        sharedPreference = getActivity().getSharedPreferences("wishlist", Context.MODE_PRIVATE);
        allWishListPlaces = new ArrayList<>();

        Map<String, ?> elements = sharedPreference.getAll();
        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            allWishListPlaces.add(place);
        }
        if (allWishListPlaces != null) {

            web = new String[allWishListPlaces.size()];
            picname = new String[allWishListPlaces.size()];

            for (int j = 0; j < allWishListPlaces.size(); j++) {
                web[j] = allWishListPlaces.get(j).getName();
                picname[j] = allWishListPlaces.get(j).getPicture();
            }
        }

        gridAdapter = new GridAdapter(getActivity(), web, picname);
        listView.setAdapter(gridAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(getActivity(), NewPlaceDetailsActivity.class);
                i.putExtra("id", allWishListPlaces.get(position).getId());
                startActivity(i);
            }
        });
        return rootView;
    }
}
