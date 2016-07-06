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
 * Created by Ripon on 9/21/15.
 */
@ContentView(R.layout.hoteldetails)
public class HotelDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.hotelapp_bar)
    private Toolbar mToolbar;

    @InjectView(R.id.hoteltab_layout)
    private TabLayout mTabLayout;

    @InjectView(R.id.hotelpager)
    private ViewPager mPager;

    private HotelDetailsPagerAdapter mAdapter;
    String name, address, description, cost;

    @InjectView(R.id.adViewHotelDetails)
    AdView adView;

    public String titles[] = {"ARRDESS", "DESCRIPTION", "COST"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.hoteldetails);
        //mToolbar = (Toolbar) findViewById(R.id.hotelapp_bar);
        setSupportActionBar(mToolbar);
        //adView = (AdView) findViewById(R.id.adViewHotelDetails);

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
        ParseObject selectedObject = HotelsActivity.allHotels.get(index);

        name = (String) selectedObject.get("name");
        address = (String) selectedObject.get("address");
        description = (String) selectedObject.get("description");
        cost = (String) selectedObject.get("cost");

        mAdapter = new HotelDetailsPagerAdapter(getSupportFragmentManager(), titles, name, address, description, cost);

        //mTabLayout = (TabLayout) findViewById(R.id.hoteltab_layout);


        //mPager = (ViewPager) findViewById(R.id.hotelpager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        //mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);
    }
}

class HotelDetailsPagerAdapter extends FragmentStatePagerAdapter {

    String[] titles;
    String name, address, description, cost;

    public HotelDetailsPagerAdapter(FragmentManager fm, String[] titles, String name, String address, String description, String cost) {

        super(fm);
        this.name = name;
        this.address = address;
        this.description = description;
        this.cost = cost;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(address);
        } else if (position == 1) {
            return DescriptionFragment.newInstanceOfDescriptionFragment(description);
        } else {
            return DescriptionFragment.newInstanceOfDescriptionFragment(cost);
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