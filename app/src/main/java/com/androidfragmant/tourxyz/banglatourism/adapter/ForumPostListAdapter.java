package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.ForumPostDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/28/15.
 */
public class ForumPostListAdapter extends RecyclerView.Adapter<ForumPostListAdapter.ForumPostViewHolder> {

    private Context context;
    private ArrayList<ForumPost> forumPosts;
    Typeface tf;

    public ForumPostListAdapter(Activity paramActivity, ArrayList<ForumPost> forumPosts) {
        this.context = paramActivity;
        this.forumPosts = forumPosts;
        this.tf = Typeface.createFromAsset(paramActivity.getAssets(),
                "font/solaimanlipi.ttf");
    }

    @Override
    public ForumPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singleforumpost,parent,false);
        return new ForumPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForumPostViewHolder holder, final int position) {
        holder.question.setTypeface(tf);
        holder.askedby.setTypeface(tf);

        holder.question.setText(forumPosts.get(position).getQuestion());
        holder.askedby.setText("পোস্ট করেছেনঃ "+forumPosts.get(position).getName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ForumPostDetailsActivity.class);
                i.putExtra("forumpost",forumPosts.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forumPosts.size();
    }


    public static class ForumPostViewHolder extends RecyclerView.ViewHolder {

        protected TextView question;
        protected TextView askedby;
        protected LinearLayout linearLayout;

        public ForumPostViewHolder(View itemView) {
            super(itemView);
            question = ViewHolder.get(itemView,R.id.tvQuestionOfForumPost);
            askedby = ViewHolder.get(itemView,R.id.tvAskedByWhom);
            linearLayout = ViewHolder.get(itemView,R.id.forumpostlinearlayout);
        }
    }
}
