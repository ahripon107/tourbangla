package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<CharSequence> names;
    private ArrayList<CharSequence> comments;
    Context context;
    Typeface tf;

    public CommentAdapter(Context context,ArrayList<CharSequence> names, ArrayList<CharSequence> comments) {
        this.names = names;
        this.comments = comments;
        this.context = context;
        tf = Typeface.createFromAsset(context.getAssets(), "font/solaimanlipi.ttf");
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
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commenter;
        TextView comment;

        public CommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v,R.id.tvName);
            comment = ViewHolder.get(v,R.id.tvComment);
        }
    }
}
