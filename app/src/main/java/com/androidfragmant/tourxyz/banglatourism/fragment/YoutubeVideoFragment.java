package com.androidfragmant.tourxyz.banglatourism.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.YoutubePlayerActivity;
import com.androidfragmant.tourxyz.banglatourism.model.YoutubeVideo;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 8/6/16.
 */
public class YoutubeVideoFragment extends Fragment {

    ArrayList<YoutubeVideo> youtubeVideos;
    String url;
    RecyclerView recyclerView;

    public YoutubeVideoFragment() {

    }

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
        View view = inflater.inflate(R.layout.youtube_video_list, container, false);
        youtubeVideos = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.youtubevideolist);

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

        int id = getArguments().getInt("id");
        Log.d(Constants.TAG, id+" placeid");
        RequestParams params = new RequestParams();
        params.add(Constants.KEY, Constants.KEY_VALUE);

        params.add("id", id + "");

        url = Constants.FETCH_YOUTUBE_VIDEOS_URL;
        Log.d(Constants.TAG, url);

        FetchFromWeb.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), statusCode + "Failed", Toast.LENGTH_LONG).show();
            }
        });
        return view;
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
