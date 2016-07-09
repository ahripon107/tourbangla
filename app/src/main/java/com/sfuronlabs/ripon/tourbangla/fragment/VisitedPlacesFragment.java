package com.sfuronlabs.ripon.tourbangla.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.activities.BrowseByDivisionActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.GridAdapter;
import com.sfuronlabs.ripon.tourbangla.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ripon on 9/26/15.
 */
public class VisitedPlacesFragment extends Fragment {
    ListView listView;

    String[] web = new String[0];
    String[] picname = new String[0];
    ArrayList<Place> allPlaces;
    GridAdapter gridAdapter;

    public VisitedPlacesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
                web, picname, "font/Amaranth-Bold.ttf");
        listView.setAdapter(gridAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentvisitedplaces, container, false);
        listView = (ListView) rootView.findViewById(R.id.gridviewvisitedplaces);

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
                web, picname, "font/Amaranth-Bold.ttf");
        listView.setAdapter(gridAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent("android.intent.action.NEWPLACEDETAILSACTIVITY");
                i.putExtra("id", allPlaces.get(position).getId());
                startActivity(i);
            }
        });
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
