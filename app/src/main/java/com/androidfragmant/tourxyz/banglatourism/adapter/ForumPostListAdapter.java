package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.ForumPostDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class ForumPostListAdapter extends RecyclerView.Adapter<ForumPostListAdapter.ForumPostViewHolder> {
    private Context context;
    private ArrayList<ForumPost> forumPosts;
    private Typeface tf;

    public ForumPostListAdapter(Context context, ArrayList<ForumPost> forumPosts) {
        this.context = context;
        this.forumPosts = forumPosts;
        this.tf = Constants.solaimanLipiFont(context);
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
        holder.timeStamp.setText(Constants.getTimeAgo(Long.parseLong(forumPosts.get(position).getTimestamp())));
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


    static class ForumPostViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected TextView askedby;
        protected TextView timeStamp;
        protected LinearLayout linearLayout;

        public ForumPostViewHolder(View itemView) {
            super(itemView);
            question = ViewHolder.get(itemView,R.id.tvQuestionOfForumPost);
            askedby = ViewHolder.get(itemView,R.id.tvAskedByWhom);
            timeStamp = ViewHolder.get(itemView,R.id.forum_post_time_stamp);
            linearLayout = ViewHolder.get(itemView,R.id.forumpostlinearlayout);
        }
    }
}
