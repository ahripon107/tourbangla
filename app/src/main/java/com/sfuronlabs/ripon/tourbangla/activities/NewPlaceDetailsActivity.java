package com.sfuronlabs.ripon.tourbangla.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.SharedPreference;
import com.sfuronlabs.ripon.tourbangla.fragment.CommentAddComment;
import com.sfuronlabs.ripon.tourbangla.fragment.DescriptionFragment;
import com.sfuronlabs.ripon.tourbangla.fragment.WishListFragment;
import com.sfuronlabs.ripon.tourbangla.model.Place;
import com.sfuronlabs.ripon.tourbangla.view.cpb.CircularProgressButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/22/15.
 */
@ContentView(R.layout.newplacedetails)
public class NewPlaceDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.root_coordinator)
    private CoordinatorLayout mCoordinator;

    @InjectView(R.id.collapsing_toolbar_layout)
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @InjectView(R.id.app_bar)
    private Toolbar mToolbar;

    @InjectView(R.id.view_pager)
    private ViewPager mPager;

    @InjectView(R.id.tab_layout)
    private TabLayout mTabLayout;

    @InjectView(R.id.placeimage)
    ImageView imageView;

    @InjectView(R.id.btnAddToFavourite)
    CircularProgressButton addToFavourite;

    @InjectView(R.id.btnBeenThere)
    CircularProgressButton beenThere;

    @InjectView(R.id.adViewPlaceDetails)
    AdView adView;

    private CharSequence Titles[] = {"DESCRIPTION", "HOW TO GO", "HOTELS", "OTHER INFO", "COMMENTS", "MAPS"};
    private Place selectedPlace;
    SharedPreference sharedPreference;
    List<Place> favourites;
    ParseObject selectedObject;
    int index;
    ArrayList<CharSequence> names;
    ArrayList<CharSequence> comments;
    int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = new ArrayList<>();
        comments = new ArrayList<>();
        counter = 0;
        sharedPreference = new SharedPreference();
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map123));

                if (mapFragment != null) {
                    FragmentManager fM = getSupportFragmentManager();
                    fM.beginTransaction().remove(mapFragment).commit();
                }
                finish();
            }
        });

        Intent i = getIntent();
        index = i.getExtras().getInt("index");

        selectedPlace = BrowseByDivisionActivity.finalplaces.get(index);
        String picture = selectedPlace.getPicture();
        selectedObject = selectedPlace.getParseObject();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("CommentOnPlace");
        query1.whereEqualTo("post", selectedObject);
        query1.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);

        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list1, com.parse.ParseException e) {

                if (e == null) {
                    counter++;

                    for (int i = 0; i < list1.size(); i++) {
                        if (counter > 1) break;

                        names.add((CharSequence) list1.get(i).get("commenter"));
                        comments.add((CharSequence) list1.get(i).get("comment"));
                    }
                } else {
                    Toast.makeText(NewPlaceDetailsActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                }

            }
        });
        Picasso.with(getApplicationContext()).load("http://vpn.gd/tourbangla/" + picture + ".jpg").into(imageView);
        int noOfTabs = 6;
        YourPagerAdapter mAdapter = new YourPagerAdapter(getSupportFragmentManager(), Titles, noOfTabs, selectedPlace, names, comments, selectedObject);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mPager);
        mCollapsingToolbarLayout.setTitle(selectedPlace.getName() + " , " + selectedPlace.getAddress());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

        addToFavourite.setText("ADD TO WISHLIST");
        if (sharedPreference.containsObject(NewPlaceDetailsActivity.this, selectedPlace)) {
            addToFavourite.setText("REMOVE FROM WISHLIST");
        }

        addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToFavourite.getText().toString().equals("ADD TO WISHLIST")) {
                    sharedPreference.addFavorite(NewPlaceDetailsActivity.this, selectedPlace);
                    favourites = sharedPreference.getFavorites(NewPlaceDetailsActivity.this);
                    WishListFragment.wishlist = (ArrayList) favourites;
                    new FalseProgress(addToFavourite).execute(100);
                    addToFavourite.setText("REMOVE FROM WISHLIST");
                } else {
                    sharedPreference.removeFavorite(NewPlaceDetailsActivity.this, selectedPlace);
                    favourites = sharedPreference.getFavorites(NewPlaceDetailsActivity.this);
                    WishListFragment.wishlist = (ArrayList) favourites;
                    new FalseProgress(addToFavourite).execute(100);
                    addToFavourite.setText("ADD TO WISHLIST");
                }
            }
        });
        beenThere.setText("I'VE VISITED THERE");
        final SharedPreferences sharedPreferences = this.getSharedPreferences("rating", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(selectedPlace.getName())) {

            String string = sharedPreferences.getString(selectedPlace.getName(), null);
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            int rating = place.getRating();
            if (rating == 5)
                beenThere.setText(R.string.iloveit);
            else if (rating == 4)
                beenThere.setText(R.string.ilikeit);
            else if (rating == 3)
                beenThere.setText(R.string.itsok);
            else if (rating == 2)
                beenThere.setText(R.string.dontlike);
            else if (rating == 1)
                beenThere.setText(R.string.hateit);


        }
        beenThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final SharedPreferences.Editor editor;
                editor = sharedPreferences.edit();

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(NewPlaceDetailsActivity.this);
                builderSingle.setIcon(R.drawable.ic_profile);
                builderSingle.setTitle("Rate the place:-");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        NewPlaceDetailsActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("I love it");
                arrayAdapter.add("I like it");
                arrayAdapter.add("Its ok");
                arrayAdapter.add("I dont like it");
                arrayAdapter.add("I hate it");
                arrayAdapter.add("I've not visited there");
                builderSingle.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (sharedPreferences.contains(selectedPlace.getName())) {
                                    editor.remove(selectedPlace.getName()).apply();
                                }
                                Place product = selectedPlace;
                                Place place = new Place(product.getId(), product.getName(), product.getDescription(), product.getHowtogo(), product.getLattitude(), product.getLongitude(), product.getHotel(), product.getOthers(), product.getPicture(), product.getAddress(), product.getType(), product.getDistrict(), product.getParseObject().getObjectId(), 5 - which);
                                Gson gson = new Gson();
                                String string = gson.toJson(place);
                                if (which != 5) {
                                    editor.putString(selectedPlace.getName(), string).apply();
                                }
                                new FalseProgress(beenThere).execute(100);

                                if (which == 0)
                                    beenThere.setText(R.string.iloveit);
                                else if (which == 1)
                                    beenThere.setText(R.string.ilikeit);
                                else if (which == 2)
                                    beenThere.setText(R.string.itsok);
                                else if (which == 3)
                                    beenThere.setText(R.string.dontlike);
                                else if (which == 4)
                                    beenThere.setText(R.string.hateit);
                                else
                                    beenThere.setText("I'VE VISITED THERE");
                            }
                        });
                builderSingle.show();
            }


        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map123));

        if (mapFragment != null) {
            FragmentManager fM = getSupportFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }
        finish();
    }

    private class FalseProgress extends AsyncTask<Integer, Integer, Integer> {

        private CircularProgressButton cpb;

        public FalseProgress(CircularProgressButton cpb) {
            this.cpb = cpb;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            for (int progress = 0; progress < 100; progress += 5) {
                publishProgress(progress);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            cpb.setProgress(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            cpb.setProgress(progress);
        }
    }
}

class YourPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence[] Titles;
    int NoOfTabs;
    private Place SelectedPlace;
    ArrayList<CharSequence> names, comments;
    ParseObject parseObject;

    public YourPagerAdapter(FragmentManager fm, CharSequence[] Titles, int NoOfTabs, Place selectedPlace, ArrayList<CharSequence> names, ArrayList<CharSequence> comments, ParseObject parseObject) {
        super(fm);
        this.Titles = Titles;
        this.NoOfTabs = NoOfTabs;
        this.SelectedPlace = selectedPlace;
        this.names = names;
        this.comments = comments;
        this.parseObject = parseObject;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getDescription());
        } else if (position == 1) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getHowtogo());
        } else if (position == 2) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getHotel());
        } else if (position == 3) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(SelectedPlace.getOthers());
        } else if (position == 4) {
            return CommentAddComment.NewInstanceofCommentAddComment(names, comments, parseObject.getObjectId(), 1);
        } else {
            return MapsActivity.NewInstanceOfMapsActivity(SelectedPlace.getLattitude(), SelectedPlace.getLongitude());
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


}

