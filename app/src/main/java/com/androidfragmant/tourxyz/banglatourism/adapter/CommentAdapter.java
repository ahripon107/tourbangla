package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> comments;
    private ArrayList<String> timestamps;
    Context context;
    Typeface tf;

    public CommentAdapter(Context context, ArrayList<String> names, ArrayList<String> comments, ArrayList<String> timestamps) {
        this.names = names;
        this.comments = comments;
        this.timestamps = timestamps;
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.comment.setTypeface(tf);
        holder.commenter.setTypeface(tf);
        holder.commenter.setText("মন্তব্য করেছেন:  " + names.get(position));
        holder.comment.setText(comments.get(position));
        holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(timestamps.get(position))));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;

        public CommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
        }
    }
}
