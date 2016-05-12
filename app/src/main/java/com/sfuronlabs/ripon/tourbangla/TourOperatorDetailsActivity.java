package com.sfuronlabs.ripon.tourbangla;

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
import com.sfuronlabs.ripon.tourbangla.fragment.DescriptionFragment;

/**
 * Created by Ripon on 8/28/15.
 */
public class TourOperatorDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private TourOperatorDetailsPagerAdapter mAdapter;
    String name,address,places,others;
    public String titles[] = {"ARRDESS","PLACES","OTHERS"};
    AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuoroperatordetails);
        mToolbar = (Toolbar) findViewById(R.id.touroperatorapp_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adView = (AdView) findViewById(R.id.adViewTourOperatorDetails);

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

        mTabLayout = (TabLayout) findViewById(R.id.touroperatortab_layout);


        mPager = (ViewPager) findViewById(R.id.touroperatorpager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        //mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        Log.d("here", "complete");

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("18D9D4FB40DF048C506091E42E0FDAFD").build();
        adView.loadAd(adRequest);

    }
}

class TourOperatorDetailsPagerAdapter extends FragmentStatePagerAdapter {

    String[] titles;
    String name, address, places, others;

    public TourOperatorDetailsPagerAdapter(FragmentManager fm,String[] titles,String name, String address, String places, String others) {

        super(fm);
        this.name = name;
        this.address = address;
        this.places = places;
        this.others = others;
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
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstanceOfDescriptionFragment(places);
            return descriptionFragment;
        }
        else
        {
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstanceOfDescriptionFragment(others);
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