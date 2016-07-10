package com.sfuronlabs.ripon.tourbangla.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.TourOperatorOfferRecyclerAdapter;
import com.sfuronlabs.ripon.tourbangla.model.TourOperatorOffer;
import com.sfuronlabs.ripon.tourbangla.util.Constants;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 7/6/16.
 */
@ContentView(R.layout.browsebydivision)
public class TourOperatorOffersListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.adViewDivision)
    AdView adView;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.gridview)
    RecyclerView recyclerView;

    @InjectView(R.id.pwDhaka)
    ProgressWheel progressWheel;

    ArrayList<TourOperatorOffer> offers;
    TourOperatorOfferRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        offers = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Tour Offers");
        adapter = new TourOperatorOfferRecyclerAdapter(TourOperatorOffersListActivity.this,offers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TourOperatorOffersListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();

        offers.clear();
        adapter.notifyDataSetChanged();

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);

        String url = "http://apisea.xyz/TourBangla/tourOperatorOffer.php?key=bl905577";
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

                        String title = jsonObject.getString("title");
                        String summary = jsonObject.getString("summary");
                        String details = jsonObject.getString("details");
                        String imageName = jsonObject.getString("imageName");
                        TourOperatorOffer operatorOffer = new TourOperatorOffer(title,summary
                                ,details,imageName);
                        offers.add(operatorOffer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(TourOperatorOffersListActivity.this, statusCode+"failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
