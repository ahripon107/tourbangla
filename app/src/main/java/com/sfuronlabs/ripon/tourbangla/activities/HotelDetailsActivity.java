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
import com.sfuronlabs.ripon.tourbangla.fragment.DescriptionFragment;

/**
 * Created by Ripon on 9/21/15.
 */
public class HotelDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private HotelDetailsPagerAdapter mAdapter;
    String name,address,description,cost;
    AdView adView;
    public String titles[] = {"ARRDESS","DESCRIPTION","COST"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoteldetails);
        mToolbar = (Toolbar) findViewById(R.id.hotelapp_bar);
        setSupportActionBar(mToolbar);
        adView = (AdView) findViewById(R.id.adViewHotelDetails);

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

        mTabLayout = (TabLayout) findViewById(R.id.hoteltab_layout);


        mPager = (ViewPager) findViewById(R.id.hotelpager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        //mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("18D9D4FB40DF048C506091E42E0FDAFD").build();
        adView.loadAd(adRequest);
    }
}

class HotelDetailsPagerAdapter extends FragmentStatePagerAdapter {

    String[] titles;
    String name, address, description, cost;

    public HotelDetailsPagerAdapter(FragmentManager fm,String[] titles,String name, String address, String description, String cost) {

        super(fm);
        this.name = name;
        this.address = address;
        this.description = description;
        this.cost = cost;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstanceOfDescriptionFragment(address);
            return descriptionFragment;
        }
        else if (position==1)
        {
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstanceOfDescriptionFragment(description);
            return descriptionFragment;
        }
        else
        {
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstanceOfDescriptionFragment(cost);
            return descriptionFragment;
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