package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseUser;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.PlaceAccessHelper;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.GoogleCardsTravelAdapter;
import com.sfuronlabs.ripon.tourbangla.model.Place;
import com.sfuronlabs.ripon.tourbangla.util.Constants;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
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

    private GoogleCardsTravelAdapter mGoogleCardsAdapter;


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

        String divisionName = getIntent().getStringExtra("divisionName");
        String districtName = getIntent().getStringExtra("districtName");
        Log.d(Constants.TAG, divisionName);
        Log.d(Constants.TAG, districtName);

        setTitle("Browse By Division");
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(BrowseByDivisionActivity.this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BrowseByDivisionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mGoogleCardsAdapter = new GoogleCardsTravelAdapter(BrowseByDivisionActivity.this, PlaceAccessHelper.getPlacesOfDistrict(districtName));
        recyclerView.setAdapter(mGoogleCardsAdapter);



        //mGoogleCardsAdapter.notifyDataSetChanged();


        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
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
