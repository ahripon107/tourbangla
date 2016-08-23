package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.TourOperatorOffer;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.view.ProgressWheel;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Typeface tf;

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
        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        recyclerView.setAdapter(new AbstractListAdapter<TourOperatorOffer,TourOperatorOfferViewHolder>(offers) {
            @Override
            public TourOperatorOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singletouroperatoroffer,parent,false);
                return new TourOperatorOfferViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TourOperatorOfferViewHolder holder, int position) {
                final TourOperatorOffer offer = offers.get(position);
                holder.offerTitle.setText(offer.getTitle());
                holder.offerTitle.setTypeface(tf);
                holder.offerSummary.setText(offer.getSummary());
                Picasso.with(TourOperatorOffersListActivity.this).load(offers.get(position).getImageName()).into(holder.offerImage);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TourOperatorOffersListActivity.this, TourOfferDetailsActivity.class);
                        intent.putExtra("details",offer.getDetails());
                        intent.putExtra("link",offer.getLink());
                        startActivity(intent);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();

        offers.clear();
        recyclerView.getAdapter().notifyDataSetChanged();

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);

        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }

                if (msg.what == Constants.SUCCESS) {
                    JSONObject response = (JSONObject) msg.obj;
                    if (response != null) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Gson gson = new Gson();
                                TourOperatorOffer operatorOffer = gson.fromJson(String.valueOf(jsonObject),TourOperatorOffer.class);
                                offers.add(operatorOffer);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        Log.d(Constants.TAG, response.toString());
                    } else {
                        Toast.makeText(TourOperatorOffersListActivity.this, "Failed loading...Please try again...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TourOperatorOffersListActivity.this, "Failed loading...Please try again...", Toast.LENGTH_LONG).show();
                }
            }
        };

        FetchFromWeb fetchFromWeb = new FetchFromWeb(handler);
        fetchFromWeb.retreiveData(Constants.TOUR_OPERATOR_OFFER_URL,requestParams);

    }

    private static class TourOperatorOfferViewHolder extends RecyclerView.ViewHolder {
        protected TextView offerTitle;
        protected TextView offerSummary;
        protected Button button;
        protected ImageView offerImage;

        public TourOperatorOfferViewHolder(View itemView) {
            super(itemView);
            offerTitle = ViewHolder.get(itemView, R.id.tourOfferTitle);
            offerSummary = ViewHolder.get(itemView,R.id.tourOfferSummary);
            button = ViewHolder.get(itemView,R.id.tourOfferDetails);
            offerImage = ViewHolder.get(itemView,R.id.tourOfferImage);
        }
    }
}
