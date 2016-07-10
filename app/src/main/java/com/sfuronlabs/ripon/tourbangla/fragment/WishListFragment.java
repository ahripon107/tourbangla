package com.sfuronlabs.ripon.tourbangla.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.activities.BrowseByDivisionActivity;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.SharedPreference;
import com.sfuronlabs.ripon.tourbangla.adapter.GridAdapter;
import com.sfuronlabs.ripon.tourbangla.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 6/11/15.
 */
public class WishListFragment extends Fragment {
    ListView listView;
    SharedPreference sharedPreference;
    String[] web = new String[0];
    String[] picname = new String[0];
    GridAdapter gridAdapter;
    ArrayList<Place> allPlaces;
    public static ArrayList<Place> wishlist;

    public WishListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        allPlaces = wishlist;
        if (allPlaces.size() == wishlist.size()) {

            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];
            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
            gridAdapter = new GridAdapter(getActivity(),
                    web, picname, "font/Amaranth-Bold.ttf");
            listView.setAdapter(gridAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        listView = (ListView) rootView.findViewById(R.id.gridviewfavourite);
        sharedPreference = new SharedPreference();
        allPlaces = new ArrayList<>();
        wishlist = new ArrayList<>();
        allPlaces = sharedPreference.getFavorites(getActivity());
        if (allPlaces != null) {

            wishlist = allPlaces;
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
