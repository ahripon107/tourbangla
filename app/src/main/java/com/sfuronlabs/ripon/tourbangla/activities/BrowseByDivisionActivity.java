package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.adapter.ViewPagerAdapter;
import com.sfuronlabs.ripon.tourbangla.model.Place;

/**
 * Created by Ripon on 6/13/15.
 */
public class BrowseByDivisionActivity extends AppCompatActivity {

    AdView adView;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    TabLayout tabs;
    public static ArrayList<Place> finalplaces;
    public static ArrayList<ParseObject> objects;

    CharSequence Titles[] = {"DHAKA", "CHITTAGONG", "RAJSHAHI", "KHULNA", "BARISAL", "SYLHET", "RANGPUR"};
    int Numboftabs = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsebydivision);
        adView = (AdView) findViewById(R.id.adViewDivision);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Browse By Division");
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabsFromPagerAdapter(adapter);
        tabs.setupWithViewPager(pager);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("18D9D4FB40DF048C506091E42E0FDAFD").build();
        adView.loadAd(adRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(BrowseByDivisionActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
