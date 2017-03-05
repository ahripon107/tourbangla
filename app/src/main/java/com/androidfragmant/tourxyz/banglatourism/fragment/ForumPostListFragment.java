package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.activities.ForumPostDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.LoginActivity;
import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class ForumPostListFragment extends RoboFragment {

    @InjectView(R.id.list)
    private RecyclerView recyclerView;

    @InjectView(R.id.et_forum_post)
    private EditText forumPst;

    @InjectView(R.id.btn_send)
    private ImageButton send;

    @Inject
    private NetworkService networkService;

    private AbstractListAdapter<ForumPost, ForumPostViewHolder> forumPostListAdapter;
    private ArrayList<ForumPost> forumPosts;
    private Typeface tf;
    private Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forumPosts = new ArrayList<>();
        tf = Constants.solaimanLipiFont(getContext());

        forumPostListAdapter = new AbstractListAdapter<ForumPost, ForumPostViewHolder>(forumPosts) {
            @Override
            public ForumPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forum_post, parent, false);
                return new ForumPostViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ForumPostViewHolder holder, final int position) {
                holder.question.setTypeface(tf);
                holder.askedby.setTypeface(tf);
                holder.question.setText(forumPosts.get(position).getQuestion());
                holder.askedby.setText("পোস্ট করেছেনঃ " + forumPosts.get(position).getName());
                holder.timeStamp.setText(Constants.getTimeAgo(Long.parseLong(forumPosts.get(position).getTimestamp())));
                if (!forumPosts.get(position).getProfileimage().equals("")) {
                    Picasso.with(getContext()).load(forumPosts.get(position).getProfileimage()).into(holder.profileImage);
                }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), ForumPostDetailsActivity.class);
                        i.putExtra("forumpost", forumPosts.get(position));
                        getActivity().startActivity(i);
                        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                });
                Constants.setLeftInAnimation(holder.cardView, getContext());
                Constants.setRightInAnimation(holder.question, getContext());
            }
        };

        fetchContents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum_post_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(forumPostListAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    if (Validator.validateNotEmpty(forumPst, "Required")) {

                        networkService.insertForumPost(profile.getName(), forumPst.getText().toString().trim(), profile.getProfilePictureUri(65,65).toString(), System.currentTimeMillis() + "",
                                new DefaultMessageHandler(getContext(), true) {
                                    @Override
                                    public void onSuccess(Message msg) {
                                        forumPst.getText().clear();
                                        Toast.makeText(getContext(), "Successfully Posted", Toast.LENGTH_LONG).show();
                                        fetchContents();
                                    }
                                });
                    }
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    public void fetchContents() {

        networkService.fetchForumPostList(new DefaultMessageHandler(getContext(), true) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string);
                    forumPosts.clear();

                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        ForumPost forumPost = gson.fromJson(String.valueOf(jsonObject), ForumPost.class);
                        forumPosts.add(forumPost);

                        forumPostListAdapter.notifyDataSetChanged();

                    }
                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class ForumPostViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected TextView askedby;
        protected TextView timeStamp;
        protected LinearLayout linearLayout;
        protected CardView cardView;
        protected ImageView profileImage;

        public ForumPostViewHolder(View itemView) {
            super(itemView);
            question = ViewHolder.get(itemView, R.id.tvQuestionOfForumPost);
            askedby = ViewHolder.get(itemView, R.id.tvAskedByWhom);
            timeStamp = ViewHolder.get(itemView, R.id.forum_post_time_stamp);
            linearLayout = ViewHolder.get(itemView, R.id.forumpostlinearlayout);
            cardView = ViewHolder.get(itemView, R.id.card);
            profileImage = ViewHolder.get(itemView, R.id.profile_image_view);
        }
    }
}
