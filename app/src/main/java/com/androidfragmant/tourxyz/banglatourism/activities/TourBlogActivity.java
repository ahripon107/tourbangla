package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.tourblog)
public class TourBlogActivity extends RoboAppCompatActivity {

    @InjectView(R.id.rvAllBlogPosts)
    private RecyclerView recyclerView;

    @InjectView(R.id.fabAddNewBlog)
    private FloatingActionButton fabNewBlog;

    @InjectView(R.id.adViewTourBlog)
    private AdView adView;

    @Inject
    private ArrayList<BlogPost> blogPosts;

    @Inject
    private NetworkService networkService;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tf = Constants.solaimanLipiFont(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(new AbstractListAdapter<BlogPost, TourBlogViewHolder>(blogPosts) {
            @Override
            public TourBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tour_blog_item, parent, false);
                return new TourBlogViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TourBlogViewHolder holder, int position) {
                final BlogPost blogPost = blogPosts.get(position);
                holder.name.setTypeface(tf);
                holder.title.setTypeface(tf);
                holder.tags.setTypeface(tf);
                holder.name.setText("লিখেছেন: " + blogPost.getName());
                holder.title.setText(blogPost.getTitle());
                holder.tags.setText(blogPost.getReadtimes()+"  বার পঠিত");
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(blogPost.getTimestamp())));

                Picasso.with(TourBlogActivity.this).load(blogPost.getImage()).into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(TourBlogActivity.this, TourBlogDetailsActivity.class);
                        i.putExtra("post", blogPost);
                        startActivity(i);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                });

                Constants.setLeftInAnimation(holder.cardView,TourBlogActivity.this);
                Constants.setRightInAnimation(holder.title,TourBlogActivity.this);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(TourBlogActivity.this));

        fabNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TourBlogActivity.this, NewTourBlogActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
        networkService.fetchBlogPostList(new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    blogPosts.clear();

                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        BlogPost blogPost = gson.fromJson(String.valueOf(jsonObject), BlogPost.class);
                        blogPosts.add(blogPost);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class TourBlogViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title, name, tags;
        protected TextView timestamp;
        protected LinearLayout linearLayout;
        protected CardView cardView;

        public TourBlogViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.imgBlogPost);
            title = ViewHolder.get(itemView, R.id.txtPostTitle);
            name = ViewHolder.get(itemView, R.id.txtPostWriter);
            tags = ViewHolder.get(itemView, R.id.txtPostTags);
            timestamp = ViewHolder.get(itemView, R.id.tv_blog_time_stamp);
            linearLayout = ViewHolder.get(itemView, R.id.blogPostContainer);
            cardView = ViewHolder.get(itemView,R.id.card_bl);
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
}
