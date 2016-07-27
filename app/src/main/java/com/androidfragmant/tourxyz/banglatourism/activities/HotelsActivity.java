package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.HotelListStyle;
import com.androidfragmant.tourxyz.banglatourism.view.ProgressWheel;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 8/29/15.
 */
@ContentView(R.layout.hotels)
public class HotelsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwHotels)
    ProgressWheel progressWheel;

    @InjectView(R.id.lvHotels)
    ListView hotels;
    String[] names;

    @InjectView(R.id.hoteltool_bar)
    Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotels);
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

        Intent i = getIntent();
        final String str = i.getExtras().getString("place");
        setTitle("Hotels of "+str);
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

    }
}
