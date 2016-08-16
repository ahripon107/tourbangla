package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.view.ProgressWheel;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

    ArrayList<BlogPost> blogPosts;
    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blogPosts = new ArrayList<>();
        tf = Typeface.createFromAsset(getAssets(), Constants.SOLAIMAN_LIPI_FONT);

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

        recyclerView.setAdapter(new AbstractListAdapter<BlogPost,TourBlogViewHolder>(TourBlogActivity.this,blogPosts) {
            @Override
            public TourBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tour_blog_item,parent,false);
                return new TourBlogViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TourBlogViewHolder holder, int position) {
                final BlogPost blogPost = blogPosts.get(position);
                holder.name.setTypeface(tf);
                holder.title.setTypeface(tf);
                holder.tags.setTypeface(tf);
                holder.name.setText("লিখেছেন: "+blogPost.getName());
                holder.title.setText(blogPost.getTitle());
                holder.tags.setText("Tags: "+blogPost.getTags());
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(blogPost.getTimestamp())));

                Picasso.with(TourBlogActivity.this).load(blogPost.getImage()).into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(TourBlogActivity.this, TourBlogDetailsActivity.class);
                        i.putExtra("post",blogPost);
                        startActivity(i);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(TourBlogActivity.this));

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
                        Gson gson = new Gson();
                        BlogPost blogPost = gson.fromJson(String.valueOf(jsonObject),BlogPost.class);
                        blogPosts.add(blogPost);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
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

    static class TourBlogViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title,name,tags;
        protected TextView timestamp;
        protected LinearLayout linearLayout;

        public TourBlogViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.imgBlogPost);
            title = ViewHolder.get(itemView,R.id.txtPostTitle);
            name = ViewHolder.get(itemView,R.id.txtPostWriter);
            tags = ViewHolder.get(itemView,R.id.txtPostTags);
            timestamp = ViewHolder.get(itemView,R.id.tv_blog_time_stamp);
            linearLayout = ViewHolder.get(itemView,R.id.blogPostContainer);
        }
    }
}
