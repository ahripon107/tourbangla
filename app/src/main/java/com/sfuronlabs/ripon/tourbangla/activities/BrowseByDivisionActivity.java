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

    @InjectView(R.id.pwDhaka)
    ProgressWheel progressWheel;

    public ArrayList<Place> places;

    private GoogleCardsTravelAdapter mGoogleCardsAdapter;
    ArrayList<Place> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new ArrayList<>();
        places = new ArrayList<>();

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BrowseByDivisionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mGoogleCardsAdapter = new GoogleCardsTravelAdapter(BrowseByDivisionActivity.this, data);
        recyclerView.setAdapter(mGoogleCardsAdapter);


        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(BrowseByDivisionActivity.this));
        data.clear();
        mGoogleCardsAdapter.notifyDataSetChanged();
        final String divisionName = getIntent().getStringExtra("divisionName");
        final String districtName = getIntent().getStringExtra("districtName");
        Log.d(Constants.TAG, divisionName);
        Log.d(Constants.TAG, districtName);

        String url = Constants.FETCH_PLACES_URL;
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Place place = new Place(jsonObject.getInt("id"), jsonObject.getString("name")
                                , jsonObject.getString("description"), jsonObject.getString("howtogo"),
                                jsonObject.getString("lattitude"), jsonObject.getString("longitude"),
                                jsonObject.getString("hotel"), jsonObject.getString("others"),
                                jsonObject.getString("picture"), jsonObject.getString("district"),
                                jsonObject.getString("division"));
                        places.add(place);
                        if (place.getDivision().equals(divisionName.toUpperCase())) {
                            data.add(place);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PlaceAccessHelper.populate(places);
                mGoogleCardsAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(BrowseByDivisionActivity.this, statusCode+"failed", Toast.LENGTH_LONG).show();
            }
        });

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
