package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.fragment.TopPlacesFragment;
import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.ImageUtil;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.inject.Inject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.androidfragmant.tourxyz.banglatourism.PlaceAccessHelper;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.squareup.picasso.Picasso;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.androidfragmant.tourxyz.banglatourism.util.Constants.setLeftInAnimation;
import static com.androidfragmant.tourxyz.banglatourism.util.Constants.setRightInAnimation;

/**
 * @author Ripon
 */
@ContentView(R.layout.browsebydivision)
public class BrowseByDivisionActivity extends RoboAppCompatActivity {


    @InjectView(R.id.gridview)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<Place> places;

    private Typeface tf;

    private BrowsePlacesAdapter browsePlacesAdapter;

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

        places = PlaceAccessHelper.getPlacesOfDistrict(districtName);
        browsePlacesAdapter = new BrowsePlacesAdapter(this,places);
        recyclerView.setAdapter(browsePlacesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BrowseByDivisionActivity.this));
    }

    private static class PlaceCardViewHolder extends ParallaxViewHolder {
        protected TextView title;
        protected RelativeLayout linearLayout;

        @Override
        public int getParallaxImageId() {
            return R.id.list_item_google_cards_travel_image;
        }

        public PlaceCardViewHolder(View itemView) {
            super(itemView);
            title = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_title);
            linearLayout = ViewHolder.get(itemView,R.id.linear);
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

    private class BrowsePlacesAdapter extends RecyclerView.Adapter<PlaceCardViewHolder> {

        protected Context context;
        protected ArrayList<Place> places;

        public BrowsePlacesAdapter(Context context, ArrayList<Place> places) {
            this.context = context;
            this.places = places;
        }
        @Override
        public PlaceCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
            return new PlaceCardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaceCardViewHolder holder, int position) {
            final Place place = places.get(position);
            //setLeftInAnimation(holder.linearLayout,getContext());
            //setRightInAnimation(holder.imageView,getContext());
            holder.title.setTypeface(tf);
            Picasso.with(BrowseByDivisionActivity.this).load(place.getPicture()).into(holder.getBackgroundImage());
            holder.title.setText(place.getName());

            holder.getBackgroundImage().reuse();

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(BrowseByDivisionActivity.this, NewPlaceDetailsActivity.class);
                    i.putExtra("id", place.getId());
                    startActivity(i);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
            });
        }

        @Override
        public int getItemCount() {
            return places.size();
        }
    }

}
