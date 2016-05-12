package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

/**
 * Created by Ripon on 7/21/15.
 */
public class TourBlog extends AppCompatActivity {
    ProgressWheel progressWheel;

    ListView allBlogPosts;
    TourBlogListStyle style;
    ArrayList<String> posts;
    ArrayList<String> writers;
    ArrayList<String> tags;
    ArrayList<File> picture;
    public static ArrayList<ParseObject> retrievedObjects;
    Toolbar toolbar;
    FloatingActionButton fabNewBlog;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourblog);
        allBlogPosts = (ListView) findViewById(R.id.lvAllBlogPosts);
        progressWheel = (ProgressWheel) findViewById(R.id.pwTourBlog);
        progressWheel.spin();
        posts = new ArrayList<>();
        writers = new ArrayList<>();
        tags = new ArrayList<>();
        picture = new ArrayList<>();
        retrievedObjects = new ArrayList<>();
        fabNewBlog = (FloatingActionButton) findViewById(R.id.fabAddNewBlog);
        adView = (AdView) findViewById(R.id.adViewTourBlog);

        toolbar = (Toolbar) findViewById(R.id.toolbarTourBlog);
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
        //final ProgressDialog dialog = ProgressDialog.show(TourBlog.this, "Loading", "Please wait...", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> parseObjects, com.parse.ParseException e) {
                //dialog.dismiss();
                if (progressWheel.isSpinning())
                {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                if (e == null) {
                    retrievedObjects = (ArrayList) parseObjects;

                    style = new TourBlogListStyle(TourBlog.this, parseObjects, "font/solaimanlipi.ttf");
                    allBlogPosts.setAdapter(style);


                } else {
                    Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        allBlogPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TourBlog.this, TourBlogDetailsActivity.class);
                i.putExtra("index", position);
                startActivity(i);
            }
        });

        fabNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent i = new Intent(TourBlog.this, NewTourBlog.class);
                        startActivity(i);
                    }



        });

        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("D3FA0144AD5EA91460638306E4CB0FB2").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
