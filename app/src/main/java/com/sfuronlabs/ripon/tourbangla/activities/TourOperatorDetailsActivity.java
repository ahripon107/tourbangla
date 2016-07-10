package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.parse.ParseObject;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.fragment.DescriptionFragment;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/28/15.
 */
@ContentView(R.layout.tuoroperatordetails)
public class TourOperatorDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.touroperatorapp_bar)
    private Toolbar mToolbar;

    @InjectView(R.id.touroperatortab_layout)
    private TabLayout mTabLayout;

    @InjectView(R.id.touroperatorpager)
    private ViewPager mPager;

    @InjectView(R.id.adViewTourOperatorDetails)
    AdView adView;

    private TourOperatorDetailsPagerAdapter mAdapter;
    String name, address, places, others;
    public String titles[] = {"ARRDESS", "PLACES", "OTHERS"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        String str = i.getExtras().getString("namet");
        int index = i.getExtras().getInt("index");
        ParseObject selectedObject = SelectTourOperatorActivity.allOperators.get(index);

        setTitle(str);
        name = (String) selectedObject.get("name");
        address = (String) selectedObject.get("address");
        places = (String) selectedObject.get("places");
        others = (String) selectedObject.get("others");
        mAdapter = new TourOperatorDetailsPagerAdapter(getSupportFragmentManager(), titles, name, address, places, others);

        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);

        Log.d("here", "complete");

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);

    }
}

class TourOperatorDetailsPagerAdapter extends FragmentStatePagerAdapter {

    String[] titles;
    String name, address, places, others;

    public TourOperatorDetailsPagerAdapter(FragmentManager fm, String[] titles, String name, String address, String places, String others) {

        super(fm);
        this.name = name;
        this.address = address;
        this.places = places;
        this.others = others;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(address);
        } else if (position == 1) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(places);
        } else {
            return DescriptionFragment.newInstanceOfDescriptionFragment(others);
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}