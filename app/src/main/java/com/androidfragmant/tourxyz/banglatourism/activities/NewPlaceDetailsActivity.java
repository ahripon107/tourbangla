package com.androidfragmant.tourxyz.banglatourism.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidfragmant.tourxyz.banglatourism.fragment.MapsFragment;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.androidfragmant.tourxyz.banglatourism.PlaceAccessHelper;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.CommentAddComment;
import com.androidfragmant.tourxyz.banglatourism.fragment.DescriptionFragment;
import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.androidfragmant.tourxyz.banglatourism.view.cpb.CircularProgressButton;
import com.squareup.picasso.Picasso;

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

    List<Place> favourites;

    int id;
    int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counter = 0;

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
        id = i.getExtras().getInt("id");

        selectedPlace = PlaceAccessHelper.getPlace(id);
        String picture = selectedPlace.getPicture();


        Picasso.with(getApplicationContext()).load("http://vpn.gd/banglatourism/" + picture + ".jpg").into(imageView);
        int noOfTabs = 6;
        YourPagerAdapter mAdapter = new YourPagerAdapter(getSupportFragmentManager(), Titles, noOfTabs, selectedPlace);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mPager);
        mCollapsingToolbarLayout.setTitle(selectedPlace.getName());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

        addToFavourite.setText("ADD TO WISHLIST");
        SharedPreferences sharedPreferenc = this.getSharedPreferences("wishlist", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editr = sharedPreferenc.edit();
        if (sharedPreferenc.contains(selectedPlace.getName())) {
            Log.d(Constants.TAG, "exist");
            addToFavourite.setText("REMOVE FROM WISHLIST");
        }

        addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToFavourite.getText().toString().equals("ADD TO WISHLIST")) {
                    Gson gson = new Gson();
                    String string = gson.toJson(selectedPlace);
                    editr.putString(selectedPlace.getName(), string).apply();
                    new FalseProgress(addToFavourite).execute(100);
                    addToFavourite.setText("REMOVE FROM WISHLIST");
                } else {
                    editr.remove(selectedPlace.getName()).apply();
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
                                Place place = new Place(product.getId(), product.getName(), product.getDescription(), product.getHowtogo(), product.getLattitude(), product.getLongitude(), product.getHotel(), product.getOthers(), product.getPicture(), product.getDivision(), product.getDistrict(), 5 - which);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.facebook_share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                ShareDialog shareDialog = new ShareDialog(NewPlaceDetailsActivity.this);
                if (ShareDialog.canShow(ShareLinkContent.class))
                {
                    String appPackageName = getPackageName();
                    ShareLinkContent linkContent;
                    String aURL = "https://play.google.com/store/apps/details?id=" + appPackageName;
                    if(aURL == null)
                    {
                        linkContent = new ShareLinkContent.Builder().build();
                    }
                    else
                    {
                        linkContent = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(aURL))
                                .build();
                    }

                    shareDialog.show(linkContent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public YourPagerAdapter(FragmentManager fm, CharSequence[] Titles, int NoOfTabs, Place selectedPlace) {
        super(fm);
        this.Titles = Titles;
        this.NoOfTabs = NoOfTabs;
        this.SelectedPlace = selectedPlace;
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
            return CommentAddComment.NewInstanceofCommentAddComment(SelectedPlace.getId(), 1);
        } else {
            return MapsFragment.NewInstanceOfMapsActivity(SelectedPlace.getLattitude(), SelectedPlace.getLongitude());
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

