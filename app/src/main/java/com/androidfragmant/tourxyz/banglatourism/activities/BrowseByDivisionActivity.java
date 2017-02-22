package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.ImageUtil;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.inject.Inject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.PlaceAccessHelper;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.browsebydivision)
public class BrowseByDivisionActivity extends RoboAppCompatActivity {

    @InjectView(R.id.adViewDivision)
    private AdView adView;


    @InjectView(R.id.gridview)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<Place> places;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String divisionName = getIntent().getStringExtra(Constants.DIVISION_NAME);
        String districtName = getIntent().getStringExtra(Constants.DISTRICT_NAME);
        Log.d(Constants.TAG, divisionName);
        Log.d(Constants.TAG, districtName);
        tf = Constants.solaimanLipiFont(this);

        setTitle("Place List Of "+districtName);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(BrowseByDivisionActivity.this));

        places = PlaceAccessHelper.getPlacesOfDistrict(districtName);
        recyclerView.setAdapter(new AbstractListAdapter<Place,PlaceCardViewHolder>(places) {
            @Override
            public PlaceCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place,parent,false);
                return new PlaceCardViewHolder(view);
            }

            @Override
            public void onBindViewHolder(PlaceCardViewHolder holder, int position) {
                final Place place = places.get(position);
                holder.title.setTypeface(tf);
                ImageUtil.displayImage(holder.imageView, place.getPicture(), null);
                holder.title.setText(place.getName());

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BrowseByDivisionActivity.this, NewPlaceDetailsActivity.class);
                        i.putExtra("id", place.getId());
                        startActivity(i);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(BrowseByDivisionActivity.this));

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    private static class PlaceCardViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected LinearLayout linearLayout;

        public PlaceCardViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_image);
            title = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_title);
            linearLayout = ViewHolder.get(itemView,R.id.linear);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
