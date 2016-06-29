package com.sfuronlabs.ripon.tourbangla.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.activities.BrowseByDivisionActivity;
import com.sfuronlabs.ripon.tourbangla.model.Place;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.adapter.GoogleCardsTravelAdapter;
import com.sfuronlabs.ripon.tourbangla.model.DummyModel;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 6/12/15.
 */
public class DhakaFragment extends Fragment {

    ProgressWheel progressWheel;

    private static final int INITIAL_DELAY_MILLIS = 300;
    private GoogleCardsTravelAdapter mGoogleCardsAdapter;
    ArrayList<DummyModel> data;

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    public DhakaFragment() {
        BrowseByDivisionActivity.finalplaces = new ArrayList<Place>();
        BrowseByDivisionActivity.objects = new ArrayList<>();
    }

    public static DhakaFragment newInstanceOfDhakaFragment(String text) {
        DhakaFragment myFragment = new DhakaFragment();
        Bundle arguments = new Bundle();
        arguments.putString("distname", text);

        myFragment.setArguments(arguments);
        return myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragmentdhaka, container, false);
        data = new ArrayList<>();
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.pwDhaka);
        progressWheel.spin();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));


        recyclerView = (RecyclerView) rootView.findViewById(R.id.gridview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        mGoogleCardsAdapter = new GoogleCardsTravelAdapter(getActivity(), data);
        recyclerView.setAdapter(mGoogleCardsAdapter);

        recyclerView.setClipToPadding(false);
        //recyclerView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        //recyclerView.setDividerHeight(px);
        recyclerView.setFadingEdgeLength(0);
        //view.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        recyclerView.setPadding(px, px, px, px);
        recyclerView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fabDhaka);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseByDivisionActivity.finalplaces.clear();
                BrowseByDivisionActivity.objects.clear();
                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.spin();
                data.clear();
                mGoogleCardsAdapter.notifyDataSetChanged();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceTable");
                //query.whereEqualTo("district", "DHAKA");
                query.whereEqualTo("district", getArguments().getString("distname"));
                //final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> list, ParseException e) {

                        //dialog.dismiss();
                        if (progressWheel.isSpinning())
                        {
                            progressWheel.stopSpinning();
                            progressWheel.setVisibility(View.INVISIBLE);
                        }
                        if (e==null)
                        {
                            BrowseByDivisionActivity.objects = (ArrayList)list;

                            for (int i = 0; i < list.size(); i++) {
                                BrowseByDivisionActivity.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                        ,(String) list.get(i).get("description"),(String) list.get(i).get("howtogo"),
                                        (String) list.get(i).get("lattitude"),(String) list.get(i).get("longitude"),
                                        (String) list.get(i).get("hotel"),(String) list.get(i).get("others"),
                                        (String) list.get(i).get("picture"),(String) list.get(i).get("address"),
                                        (String) list.get(i).get("placetype"), (String) list.get(i).get("district"),list.get(i)));
                                DummyModel model = new DummyModel((int) list.get(i).get("id"),"http://vpn.gd/tourbangla/"+(String)list.get(i).get("picture")+".jpg",(String) list.get(i).get("name"),R.string.fontello_heart_empty);
                                data.add(model);
                            }

                            mGoogleCardsAdapter.notifyDataSetChanged();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Error occured",Toast.LENGTH_LONG).show();
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
            BrowseByDivisionActivity.finalplaces.clear();
            BrowseByDivisionActivity.objects.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceTable");
            query.whereEqualTo("district", getArguments().getString("distname"));
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {

                    if (progressWheel.isSpinning())
                    {
                        progressWheel.stopSpinning();
                        progressWheel.setVisibility(View.INVISIBLE);
                    }
                    if (e==null)
                    {
                        BrowseByDivisionActivity.objects = (ArrayList)list;
                        for (int i = 0; i < list.size(); i++) {
                            BrowseByDivisionActivity.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                    ,(String) list.get(i).get("description"),(String) list.get(i).get("howtogo"),
                                    (String) list.get(i).get("lattitude"),(String) list.get(i).get("longitude"),
                                    (String) list.get(i).get("hotel"),(String) list.get(i).get("others"),
                                    (String) list.get(i).get("picture"),(String) list.get(i).get("address"),
                                    (String) list.get(i).get("placetype"), (String) list.get(i).get("district"),list.get(i)));
                            DummyModel model = new DummyModel((int) list.get(i).get("id"),"http://vpn.gd/tourbangla/"+(String)list.get(i).get("picture")+".jpg",(String) list.get(i).get("name"),R.string.fontello_heart_empty);
                            data.add(model);
                        }

                        mGoogleCardsAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Error occured",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {  }
    }
}
