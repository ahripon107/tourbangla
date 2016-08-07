package com.androidfragmant.tourxyz.banglatourism.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.PlaceAccessHelper;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.PlaceRecyclerAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 6/13/15.
 */
@ContentView(R.layout.browsebydivision)
public class BrowseByDivisionActivity extends RoboAppCompatActivity {

    @InjectView(R.id.adViewDivision)
    AdView adView;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.gridview)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String divisionName = getIntent().getStringExtra(Constants.DIVISION_NAME);
        String districtName = getIntent().getStringExtra(Constants.DISTRICT_NAME);
        Log.d(Constants.TAG, divisionName);
        Log.d(Constants.TAG, districtName);

        setTitle("Place List Of "+districtName);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(BrowseByDivisionActivity.this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BrowseByDivisionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        PlaceRecyclerAdapter placeRecyclerAdapter = new PlaceRecyclerAdapter(BrowseByDivisionActivity.this, PlaceAccessHelper.getPlacesOfDistrict(districtName));
        recyclerView.setAdapter(placeRecyclerAdapter);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);

    }

}
