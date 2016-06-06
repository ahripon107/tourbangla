package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.adapter.ViewPagerAdapterType;

/**
 * Created by Ripon on 6/15/15.
 */
public class BrowseByTypeActivity extends AppCompatActivity{
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapterType adapter;
    TabLayout tabs;
    CharSequence Titles[]={"ARCHEOLOGICAL","BEACH","FOREST","HILLS","ISLANDS","LAKES","FALLS","OTHERS"};
    int Numboftabs =8;
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsebytype);
        adView = (AdView) findViewById(R.id.adViewType);
        toolbar = (Toolbar) findViewById(R.id.toolbartype);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Browse By Type");
        adapter = new ViewPagerAdapterType(getSupportFragmentManager(),Titles,Numboftabs);
        pager = (ViewPager) findViewById(R.id.pagertype);
        pager.setAdapter(adapter);
        tabs = (TabLayout) findViewById(R.id.tabstype);

        tabs.setTabsFromPagerAdapter(adapter);
        tabs.setupWithViewPager(pager);
        //pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("18D9D4FB40DF048C506091E42E0FDAFD").build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.logout)
        {
            ParseUser.logOut();
            Intent intent = new Intent(BrowseByTypeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
