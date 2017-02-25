package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.fragment.DescriptionFragment;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.hoteldetails)
public class HotelDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.hoteltab_layout)
    private TabLayout mTabLayout;

    @InjectView(R.id.hotelpager)
    private ViewPager mPager;

    private HotelDetailsPagerAdapter mAdapter;
    String name, address, description, cost;


    public String titles[] = {"ARRDESS", "DESCRIPTION", "COST"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String str = i.getExtras().getString("namet");
        int index = i.getExtras().getInt("index");


        mAdapter = new HotelDetailsPagerAdapter(getSupportFragmentManager(), titles, name, address, description, cost);

        //mTabLayout = (TabLayout) findViewById(R.id.hoteltab_layout);


        //mPager = (ViewPager) findViewById(R.id.hotelpager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        //mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    public class HotelDetailsPagerAdapter extends FragmentStatePagerAdapter {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

