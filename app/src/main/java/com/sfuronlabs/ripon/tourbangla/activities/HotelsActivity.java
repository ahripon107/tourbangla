package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.HotelListStyle;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

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

    @InjectView(R.id.adViewHotel)
    AdView adView;

    public static ArrayList<ParseObject> allHotels;

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
        ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("Hotel");
        parseObjectParseQuery.whereEqualTo("division", str);

        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (progressWheel.isSpinning())
                {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                if (e==null)
                {
                    names = new String[list.size()];

                    allHotels = (ArrayList) list;
                    for (int i = 0; i < list.size(); i++) {
                        names[i] = (String) list.get(i).get("name");

                    }
                    HotelListStyle hotelListStyle=new HotelListStyle(HotelsActivity.this, names);
                    hotels.setAdapter(hotelListStyle);
                }
                else
                {
                    Toast.makeText(HotelsActivity.this,"Error occured",Toast.LENGTH_LONG).show();
                }
            }

        });

        hotels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HotelsActivity.this, HotelDetailsActivity.class);
                i.putExtra("index", position);
                startActivity(i);
                return;
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);
    }
}
