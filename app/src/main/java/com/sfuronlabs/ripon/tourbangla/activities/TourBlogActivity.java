package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.adapter.TourBlogListStyle;
import com.sfuronlabs.ripon.tourbangla.adapter.TourBlogRecyclerAdapter;
import com.sfuronlabs.ripon.tourbangla.model.BlogPost;
import com.sfuronlabs.ripon.tourbangla.util.Constants;
import com.sfuronlabs.ripon.tourbangla.view.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 7/21/15.
 */
@ContentView(R.layout.tourblog)
public class TourBlogActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwTourBlog)
    ProgressWheel progressWheel;

    @InjectView(R.id.rvAllBlogPosts)
    RecyclerView recyclerView;

    @InjectView(R.id.toolbarTourBlog)
    Toolbar toolbar;

    @InjectView(R.id.fabAddNewBlog)
    FloatingActionButton fabNewBlog;

    @InjectView(R.id.adViewTourBlog)
    AdView adView;

    TourBlogRecyclerAdapter tourBlogRecyclerAdapter;
    ArrayList<BlogPost> blogPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blogPosts = new ArrayList<>();



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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TourBlogActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        tourBlogRecyclerAdapter = new TourBlogRecyclerAdapter(TourBlogActivity.this, blogPosts);
        recyclerView.setAdapter(tourBlogRecyclerAdapter);

        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();

        String url = Constants.FETCH_BLOG_POSTS_URL;
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String name = jsonObject.getString("name");
                        String details = jsonObject.getString("details");
                        String tags = jsonObject.getString("tags");
                        String image = jsonObject.getString("image");
                        BlogPost blogPost = new BlogPost(id,name,title,details,tags,image);
                        blogPosts.add(blogPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tourBlogRecyclerAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(TourBlogActivity.this, statusCode+" failed", Toast.LENGTH_LONG).show();
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
