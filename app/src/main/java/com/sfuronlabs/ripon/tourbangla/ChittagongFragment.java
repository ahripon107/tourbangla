package com.sfuronlabs.ripon.tourbangla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 6/12/15.
 */
public class ChittagongFragment extends Fragment{

    ArrayList<Place> thisplaces;

    ListView view;
    FloatingActionButton floatingActionButton;
    String[] web;
    int counter =0;
    String[] picname;
    public ChittagongFragment() {


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragmentchittagong, container, false);

        view = (ListView) rootView.findViewById(R.id.gridviewchittagong);



        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent i = new Intent("android.intent.action.PLACEDETAILS");
                Intent i = new Intent("android.intent.action.NEWPLACEDETAILSACTIVITY");
                i.putExtra("index", position);
                startActivity(i);
                return;

            }
        });

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fabChittagong);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceTable");
                query.whereEqualTo("district", "CHITTAGONG");
                final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> list, ParseException e) {
                        dialog.dismiss();
                        if (e==null)
                        {
                            BrowseByDivision.objects = (ArrayList) list;

                            for (int i = 0; i < list.size(); i++) {
                                BrowseByDivision.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                        , (String) list.get(i).get("description"), (String) list.get(i).get("howtogo"),
                                        (String) list.get(i).get("lattitude"), (String) list.get(i).get("longitude"),
                                        (String) list.get(i).get("hotel"), (String) list.get(i).get("others"),
                                        (String) list.get(i).get("picture"), (String) list.get(i).get("address"),
                                        (String) list.get(i).get("placetype"), (String) list.get(i).get("district"),list.get(i)));
                            }
                            web = new String[BrowseByDivision.finalplaces.size()];
                            picname = new String[BrowseByDivision.finalplaces.size()];

                            for (int j = 0; j < BrowseByDivision.finalplaces.size(); j++) {
                                web[j] = BrowseByDivision.finalplaces.get(j).getName();
                                picname[j] = BrowseByDivision.finalplaces.get(j).getPicture();
                            }
                            GridAdapter gridAdapter = new GridAdapter(getActivity(),
                                    web, picname, "font/Amaranth-Bold.ttf");
                            view.setAdapter(gridAdapter);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            BrowseByDivision.finalplaces.clear();
            BrowseByDivision.objects.clear();


            ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceTable");
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.whereEqualTo("district", "CHITTAGONG");
            final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {
                    dialog.dismiss();
                    if (e==null)
                    {
                        BrowseByDivision.objects = (ArrayList)list;
                        for (int i = 0; i < list.size(); i++) {
                            BrowseByDivision.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                    ,(String) list.get(i).get("description"),(String) list.get(i).get("howtogo"),
                                    (String) list.get(i).get("lattitude"),(String) list.get(i).get("longitude"),
                                    (String) list.get(i).get("hotel"),(String) list.get(i).get("others"),
                                    (String) list.get(i).get("picture"),(String) list.get(i).get("address"),
                                    (String) list.get(i).get("placetype"), (String) list.get(i).get("district"),list.get(i)));
                        }
                        web = new String[BrowseByDivision.finalplaces.size()];
                        picname = new String[BrowseByDivision.finalplaces.size()];

                        for (int j = 0; j < BrowseByDivision.finalplaces.size(); j++) {
                            web[j] = BrowseByDivision.finalplaces.get(j).getName();
                            picname[j] = BrowseByDivision.finalplaces.get(j).getPicture();
                        }
                        GridAdapter gridAdapter = new GridAdapter(getActivity(),
                                web, picname, "font/Amaranth-Bold.ttf");
                        view.setAdapter(gridAdapter);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {  }
    }
}
