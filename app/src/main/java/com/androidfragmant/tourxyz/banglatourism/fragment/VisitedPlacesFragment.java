package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidfragmant.tourxyz.banglatourism.activities.NewPlaceDetailsActivity;
import com.google.gson.Gson;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.GridAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.Place;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ripon
 */
public class VisitedPlacesFragment extends Fragment {
    ListView listView;
    String[] web = new String[0];
    String[] picname = new String[0];
    ArrayList<Place> allPlaces;
    GridAdapter gridAdapter;


    @Override
    public void onResume() {
        super.onResume();
        allPlaces = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rating", Context.MODE_PRIVATE);
        Map<String, ?> elements = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            allPlaces.add(place);
        }
        if (allPlaces != null) {
            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];
            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
        }
        gridAdapter = new GridAdapter(getActivity(),
                web, picname);
        listView.setAdapter(gridAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentvisitedplaces, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.gridviewvisitedplaces);

        allPlaces = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rating", Context.MODE_PRIVATE);
        Map<String, ?> elements = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            allPlaces.add(place);
        }
        if (allPlaces != null) {

            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];

            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
        }

        gridAdapter = new GridAdapter(getActivity(),
                web, picname);
        listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(getActivity(), NewPlaceDetailsActivity.class);
                i.putExtra("id", allPlaces.get(position).getId());
                startActivity(i);
            }
        });
    }
}
