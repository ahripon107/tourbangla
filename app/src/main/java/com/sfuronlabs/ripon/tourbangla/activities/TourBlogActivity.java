package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.TourBlogListStyle;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 7/21/15.
 */
@ContentView(R.layout.tourblog)
public class TourBlogActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwTourBlog)
    ProgressWheel progressWheel;

    @InjectView(R.id.lvAllBlogPosts)
    ListView allBlogPosts;

    @InjectView(R.id.toolbarTourBlog)
    Toolbar toolbar;

    @InjectView(R.id.fabAddNewBlog)
    FloatingActionButton fabNewBlog;

    @InjectView(R.id.adViewTourBlog)
    AdView adView;

    TourBlogListStyle style;
    ArrayList<String> posts;
    ArrayList<String> writers;
    ArrayList<String> tags;
    ArrayList<File> picture;
    public static ArrayList<ParseObject> retrievedObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressWheel.spin();
        posts = new ArrayList<>();
        writers = new ArrayList<>();
        tags = new ArrayList<>();
        picture = new ArrayList<>();

        retrievedObjects = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Tour Blog");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BlogPosts");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                if (e == null) {
                    retrievedObjects = (ArrayList) parseObjects;

                    style = new TourBlogListStyle(TourBlogActivity.this, parseObjects, "font/solaimanlipi.ttf");
                    allBlogPosts.setAdapter(style);


                } else {
                    Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        allBlogPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TourBlogActivity.this, TourBlogDetailsActivity.class);
                i.putExtra("index", position);
                startActivity(i);
            }
        });

        fabNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TourBlogActivity.this, NewTourBlogActivity.class);
                startActivity(i);
            }


        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7D3F3DF2A7214E839DBE70BE2132D5B9").build();
        adView.loadAd(adRequest);
    }
}
