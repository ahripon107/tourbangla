package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.YoutubePlayerActivity;
import com.androidfragmant.tourxyz.banglatourism.model.YoutubeVideo;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class YoutubeVideoFragment extends RoboFragment {

    @Inject
    private ArrayList<YoutubeVideo> youtubeVideos;

    @InjectView(R.id.youtubevideolist)
    private RecyclerView recyclerView;

    @Inject
    private NetworkService networkService;

    public static YoutubeVideoFragment newInstanceOfYoutubeVideoFragment(int id) {
        YoutubeVideoFragment youtubeVideoFragment = new YoutubeVideoFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("id", id);
        youtubeVideoFragment.setArguments(arguments);
        return youtubeVideoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.youtube_video_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setAdapter(new AbstractListAdapter<YoutubeVideo,VideoListViewHolder>(youtubeVideos) {
            @Override
            public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
                return new VideoListViewHolder(view);
            }

            @Override
            public void onBindViewHolder(VideoListViewHolder holder, final int position) {
                Picasso.with(getContext()).load( "http://img.youtube.com/vi/" + youtubeVideos.get(position).getUrl() + "/0.jpg" ).into(holder.imageView);
                holder.textView.setText(youtubeVideos.get(position).getTitle());
                holder.textView.setTextSize(20f);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
                        intent.putExtra("url",youtubeVideos.get(position).getUrl());
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        networkService.fetchYoutubeVideos(getArguments().getInt("id"), new DefaultMessageHandler(getContext()){
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            YoutubeVideo youtubeVideo = gson.fromJson(String.valueOf(jsonObject), YoutubeVideo.class);
                            youtubeVideos.add(youtubeVideo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class VideoListViewHolder extends RecyclerView.ViewHolder {
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
