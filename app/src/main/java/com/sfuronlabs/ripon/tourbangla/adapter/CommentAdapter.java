package com.sfuronlabs.ripon.tourbangla.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CharSequence> names;
    private  ArrayList<CharSequence> comments;

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView commenter;
        TextView comment;
        public ViewHolder(View v)
        {
            super(v);
            commenter = (TextView) v.findViewById(R.id.tvName);
            comment = (TextView) v.findViewById(R.id.tvComment);
        }
    }

    public CommentAdapter(ArrayList<CharSequence> names, ArrayList<CharSequence> comments) {

        this.names = names;
        this.comments = comments;

    }




    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView commenter = holder.commenter;
        TextView comment = holder.comment;

        commenter.setText("মন্তব্য করেছেন:  "+names.get(position));
        comment.setText(comments.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }






}
