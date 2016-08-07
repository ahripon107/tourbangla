package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.YoutubePlayerActivity;
import com.androidfragmant.tourxyz.banglatourism.model.YoutubeVideo;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/6/16.
 */
public class YoutubeVideoListAdapter extends RecyclerView.Adapter<YoutubeVideoListAdapter.VideoListViewHolder> {

    Context context;
    ArrayList<YoutubeVideo> youtubeVideos;

    public YoutubeVideoListAdapter(Context context, ArrayList<YoutubeVideo> youtubeVideos) {
        this.context = context;
        this.youtubeVideos = youtubeVideos;
    }

    @Override
    public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
        return new VideoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListViewHolder holder, final int position) {
        Picasso.with(context).load( "http://img.youtube.com/vi/" + youtubeVideos.get(position).getUrl() + "/0.jpg" ).into(holder.imageView);
        holder.textView.setText(youtubeVideos.get(position).getTitle());
        holder.textView.setTextSize(20f);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YoutubePlayerActivity.class);
                intent.putExtra("url",youtubeVideos.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    static class VideoListViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected LinearLayout linearLayout;

        public VideoListViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView, R.id.list_item_google_cards_travel_image);
            textView = ViewHolder.get(itemView, R.id.list_item_google_cards_travel_title);
            linearLayout = ViewHolder.get(itemView, R.id.linear);
        }
    }
}
