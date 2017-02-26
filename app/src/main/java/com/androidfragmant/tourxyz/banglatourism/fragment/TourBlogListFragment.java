package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.activities.NewTourBlogActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.TourBlogDetailsActivity;
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

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.tourblog)
public class TourBlogListFragment extends RoboFragment {

    @InjectView(R.id.rvAllBlogPosts)
    private RecyclerView recyclerView;

    @InjectView(R.id.fabAddNewBlog)
    private FloatingActionButton fabNewBlog;

    private ArrayList<BlogPost> blogPosts;

    @Inject
    private NetworkService networkService;

    private Typeface tf;

    private AbstractListAdapter<BlogPost, TourBlogViewHolder> tourBlogListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blogPosts = new ArrayList<>();
        tourBlogListAdapter = new AbstractListAdapter<BlogPost, TourBlogViewHolder>(blogPosts) {
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

                Picasso.with(getContext()).load(blogPost.getImage()).into(holder.imageView);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), TourBlogDetailsActivity.class);
                        i.putExtra("post", blogPost);
                        startActivity(i);
                    }
                });
                Constants.setLeftInAnimation(holder.cardView,getContext());
                Constants.setRightInAnimation(holder.title,getContext());
            }
        };

        loadPosts();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tourblog,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tf = Constants.solaimanLipiFont(getContext());

        recyclerView.setAdapter(tourBlogListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewTourBlogActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlogPost blogPost) {
        loadPosts();
    }


    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "onDestroy: ");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void loadPosts() {
        networkService.fetchBlogPostList(new DefaultMessageHandler(getContext(), true) {
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

}
