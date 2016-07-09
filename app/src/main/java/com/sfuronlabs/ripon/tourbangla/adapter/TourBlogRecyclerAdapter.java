package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.activities.TourBlogDetailsActivity;
import com.sfuronlabs.ripon.tourbangla.model.BlogPost;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ripon on 7/6/16.
 */
public class TourBlogRecyclerAdapter extends RecyclerView.Adapter<TourBlogRecyclerAdapter.TourBlogViewHolder> {

    Context context;
    ArrayList<BlogPost> blogPosts;
    Typeface tf;
    public TourBlogRecyclerAdapter(Context context, ArrayList<BlogPost> blogPosts) {
        this.context = context;
        this.blogPosts = blogPosts;
        tf = Typeface.createFromAsset(context.getAssets(), "font/solaimanlipi.ttf");
    }
    @Override
    public TourBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tourblogsingleitem,parent,false);
        return new TourBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TourBlogViewHolder holder, int position) {
        final BlogPost blogPost = blogPosts.get(position);
        holder.name.setText(blogPost.getName());
        holder.title.setText(blogPost.getTitle());
        holder.tags.setText(blogPost.getTags());
        Picasso.with(context).load("http://apisea.xyz/TourBangla/images/" + blogPost.getImage() + ".jpg").into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TourBlogDetailsActivity.class);
                i.putExtra("name", blogPost.getName());
                i.putExtra("title",blogPost.getTitle());
                i.putExtra("details",blogPost.getDetails());
                i.putExtra("tags",blogPost.getTags());
                i.putExtra("image",blogPost.getImage());
                i.putExtra("id",blogPost.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogPosts.size();
    }

    static class TourBlogViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title,name,tags;
        LinearLayout linearLayout;

        public TourBlogViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.imgBlogPost);
            title = ViewHolder.get(itemView,R.id.txtPostTitle);
            name = ViewHolder.get(itemView,R.id.txtPostWriter);
            tags = ViewHolder.get(itemView,R.id.txtPostTags);
            linearLayout = ViewHolder.get(itemView,R.id.blogPostContainer);
        }
    }
}
