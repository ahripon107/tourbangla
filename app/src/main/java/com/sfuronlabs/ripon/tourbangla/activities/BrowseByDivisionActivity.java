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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.GoogleCardsTravelAdapter;
import com.sfuronlabs.ripon.tourbangla.model.DummyModel;
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

    public static ArrayList<Place> finalplaces;
    public static ArrayList<ParseObject> objects;

    private GoogleCardsTravelAdapter mGoogleCardsAdapter;
    ArrayList<DummyModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finalplaces = new ArrayList<>();
        objects = new ArrayList<>();
        data = new ArrayList<>();
        progressWheel.spin();
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
        String divisionName = getIntent().getStringExtra("divisionName");
        String districtName = getIntent().getStringExtra("districtName");

        String url = "http://apisea.xyz/TourBangla/FetchPlaces.php?key=bl905577";
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(BrowseByDivisionActivity.this, statusCode+"success", Toast.LENGTH_LONG).show();

                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(BrowseByDivisionActivity.this, statusCode+"failed", Toast.LENGTH_LONG).show();
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceTable");

        query.whereEqualTo("district", divisionName.toUpperCase());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {

                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                if (e == null) {
                    BrowseByDivisionActivity.objects = (ArrayList) list;

                    for (int i = 0; i < list.size(); i++) {
                        BrowseByDivisionActivity.finalplaces.add(new Place((int) list.get(i).get("id"), (String) list.get(i).get("name")
                                , (String) list.get(i).get("description"), (String) list.get(i).get("howtogo"),
                                (String) list.get(i).get("lattitude"), (String) list.get(i).get("longitude"),
                                (String) list.get(i).get("hotel"), (String) list.get(i).get("others"),
                                (String) list.get(i).get("picture"), (String) list.get(i).get("address"),
                                (String) list.get(i).get("placetype"), (String) list.get(i).get("district"), list.get(i)));
                        DummyModel model = new DummyModel((int) list.get(i).get("id"), "http://vpn.gd/tourbangla/" + (String) list.get(i).get("picture") + ".jpg", (String) list.get(i).get("name"), R.string.fontello_heart_empty);
                        data.add(model);
                    }

                    mGoogleCardsAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(BrowseByDivisionActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                }
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
