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
import com.sfuronlabs.ripon.tourbangla.adapter.SingleTourOperator;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 6/18/15.
 */
@ContentView(R.layout.selecttouroperator)
public class SelectTourOperatorActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwTourOperator)
    ProgressWheel progressWheel;

    @InjectView(R.id.gridViewtouroperator)
    ListView gridView;

    @InjectView(R.id.toolbar2)
    Toolbar toolbar;

    @InjectView(R.id.adViewTourOperator)
    AdView adView;

    String[] names;
    public static ArrayList<ParseObject> allOperators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressWheel.spin();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Tour Operators");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TourOperator");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                if (e == null) {
                    names = new String[list.size()];
                    allOperators = (ArrayList) list;
                    for (int i = 0; i < list.size(); i++) {
                        names[i] = ((String) list.get(i).get("name"));
                    }
                    SingleTourOperator adapter = new SingleTourOperator(SelectTourOperatorActivity.this, names);
                    gridView.setAdapter(adapter);
                } else {
                    Toast.makeText(SelectTourOperatorActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                }

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent("android.intent.action.TOUROPERATORDETAILS");
                i.putExtra("namet", names[position]);
                i.putExtra("index", position);
                startActivity(i);
                return;
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);
    }
}
