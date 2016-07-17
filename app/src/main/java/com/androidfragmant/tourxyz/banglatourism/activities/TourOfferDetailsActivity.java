package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 7/6/16.
 */
@ContentView(R.layout.tourofferdetails)
public class TourOfferDetailsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.tvOfferDetails)
    TextView details;

    @InjectView(R.id.detailsBar)
    Toolbar toolbar;

    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tf = Typeface.createFromAsset(TourOfferDetailsActivity.this.getAssets(), "font/solaimanlipi.ttf");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Offer Details");

        Intent intent = getIntent();
        String detailsText = intent.getStringExtra("details");
        details.setTypeface(tf);
        details.setText(detailsText);
    }
}
