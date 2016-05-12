package com.sfuronlabs.ripon.tourbangla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.adapter.GoogleCardsTravelAdapter;
import com.sfuronlabs.ripon.tourbangla.model.DummyModel;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 6/12/15.
 */
public class DhakaFragment extends Fragment implements OnDismissCallback{

    ProgressWheel progressWheel;

    private static final int INITIAL_DELAY_MILLIS = 300;
    private GoogleCardsTravelAdapter mGoogleCardsAdapter;
    ArrayList<DummyModel> data;

    ListView view;
    FloatingActionButton floatingActionButton;
    String[] web;
    int counter =0;
    String[] picname;
    public DhakaFragment() {
        BrowseByDivision.finalplaces = new ArrayList<Place>();
        BrowseByDivision.objects = new ArrayList<>();
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

        view = (ListView) rootView.findViewById(R.id.gridview);
        mGoogleCardsAdapter = new GoogleCardsTravelAdapter(getActivity(), data);
        view.setAdapter(mGoogleCardsAdapter);


        /*SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(view);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);*/

        view.setClipToPadding(false);
        view.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        view.setDividerHeight(px);
        view.setFadingEdgeLength(0);
        //view.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        view.setPadding(px, px, px, px);
        view.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);


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
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fabDhaka);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseByDivision.finalplaces.clear();
                BrowseByDivision.objects.clear();
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
                            BrowseByDivision.objects = (ArrayList)list;

                            for (int i = 0; i < list.size(); i++) {
                                BrowseByDivision.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                        ,(String) list.get(i).get("description"),(String) list.get(i).get("howtogo"),
                                        (String) list.get(i).get("lattitude"),(String) list.get(i).get("longitude"),
                                        (String) list.get(i).get("hotel"),(String) list.get(i).get("others"),
                                        (String) list.get(i).get("picture"),(String) list.get(i).get("address"),
                                        (String) list.get(i).get("placetype"), (String) list.get(i).get("district"),list.get(i)));
                                DummyModel model = new DummyModel((int) list.get(i).get("id"),"http://vpn.gd/tourbangla/"+(String)list.get(i).get("picture")+".jpg",(String) list.get(i).get("name"),R.string.fontello_heart_empty);
                                data.add(model);
                            }
                            web = new String[BrowseByDivision.finalplaces.size()];
                            picname = new String[BrowseByDivision.finalplaces.size()];

                            for (int j = 0; j < BrowseByDivision.finalplaces.size(); j++) {
                                web[j] = BrowseByDivision.finalplaces.get(j).getName();
                                picname[j] = BrowseByDivision.finalplaces.get(j).getPicture();
                            }
                            mGoogleCardsAdapter.notifyDataSetChanged();
                            //GridAdapter gridAdapter = new GridAdapter(getActivity(),
                              //      web, picname, "font/Amaranth-Bold.ttf");
                            //view.setAdapter(gridAdapter);
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
            BrowseByDivision.finalplaces.clear();
            BrowseByDivision.objects.clear();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceTable");
            //query.whereEqualTo("district", "DHAKA");
            query.whereEqualTo("district", getArguments().getString("distname"));

            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
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
                        BrowseByDivision.objects = (ArrayList)list;
                        for (int i = 0; i < list.size(); i++) {
                            BrowseByDivision.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                    ,(String) list.get(i).get("description"),(String) list.get(i).get("howtogo"),
                                    (String) list.get(i).get("lattitude"),(String) list.get(i).get("longitude"),
                                    (String) list.get(i).get("hotel"),(String) list.get(i).get("others"),
                                    (String) list.get(i).get("picture"),(String) list.get(i).get("address"),
                                    (String) list.get(i).get("placetype"), (String) list.get(i).get("district"),list.get(i)));
                            DummyModel model = new DummyModel((int) list.get(i).get("id"),"http://vpn.gd/tourbangla/"+(String)list.get(i).get("picture")+".jpg",(String) list.get(i).get("name"),R.string.fontello_heart_empty);
                            data.add(model);
                        }
                        web = new String[BrowseByDivision.finalplaces.size()];
                        picname = new String[BrowseByDivision.finalplaces.size()];


                        for (int j = 0; j < BrowseByDivision.finalplaces.size(); j++) {
                            web[j] = BrowseByDivision.finalplaces.get(j).getName();
                            picname[j] = BrowseByDivision.finalplaces.get(j).getPicture();
                        }
                        mGoogleCardsAdapter.notifyDataSetChanged();
                        //GridAdapter gridAdapter = new GridAdapter(getActivity(),
                          //      web, picname, "font/Amaranth-Bold.ttf");

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

    @Override
    public void onDismiss(@NonNull final ViewGroup listView,
                          @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mGoogleCardsAdapter.remove(mGoogleCardsAdapter.getItem(position));
        }
    }

}
