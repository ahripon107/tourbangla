package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.TourOperatorOffer;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.browsebydivision)
public class TourOperatorOffersListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.adViewDivision)
    private AdView adView;

    @InjectView(R.id.gridview)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<TourOperatorOffer> offers;

    @Inject
    private NetworkService networkService;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tf = Constants.solaimanLipiFont(this);

        recyclerView.setAdapter(new AbstractListAdapter<TourOperatorOffer, TourOperatorOfferViewHolder>(offers) {
            @Override
            public TourOperatorOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singletouroperatoroffer, parent, false);
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
                        intent.putExtra("details", offer.getDetails());
                        intent.putExtra("link", offer.getLink());
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        networkService.fetchTourOperatorOffers(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Gson gson = new Gson();
                        TourOperatorOffer operatorOffer = gson.fromJson(String.valueOf(jsonObject), TourOperatorOffer.class);
                        offers.add(operatorOffer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private static class TourOperatorOfferViewHolder extends RecyclerView.ViewHolder {
        protected TextView offerTitle;
        protected TextView offerSummary;
        protected Button button;
        protected ImageView offerImage;

        public TourOperatorOfferViewHolder(View itemView) {
            super(itemView);
            offerTitle = ViewHolder.get(itemView, R.id.tourOfferTitle);
            offerSummary = ViewHolder.get(itemView, R.id.tourOfferSummary);
            button = ViewHolder.get(itemView, R.id.tourOfferDetails);
            offerImage = ViewHolder.get(itemView, R.id.tourOfferImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
