package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;


import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.TourBlogRecyclerAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.view.ProgressWheel;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
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
        recyclerView.setItemAnimator(new ScaleInBottomAnimator());
        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.getItemAnimator().setRemoveDuration(500);
        recyclerView.getItemAnimator().setMoveDuration(500);
        recyclerView.getItemAnimator().setChangeDuration(500);


        fabNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TourBlogActivity.this, NewTourBlogActivity.class);
                startActivity(i);
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);
        loadPosts();

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlogPost blogPost) {
        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void loadPosts() {
        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();

        String url = Constants.FETCH_BLOG_POSTS_URL;
        Log.d(Constants.TAG, url);

        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);

        FetchFromWeb.get(url,requestParams,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                blogPosts.clear();
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
                        tourBlogRecyclerAdapter.notifyItemInserted(blogPosts.size()-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //tourBlogRecyclerAdapter.notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                    progressWheel.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(TourBlogActivity.this, statusCode+" Failed loading posts", Toast.LENGTH_LONG).show();
            }
        });
    }
}
