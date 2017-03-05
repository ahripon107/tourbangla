package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.activities.LoginActivity;
import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.facebook.Profile;
import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;


/**
 * @author Ripon
 */
public class CommentAddComment extends RoboFragment {
    @Inject
    private ArrayList<Comment> comments;

    @InjectView(R.id.rvComments)
    private RecyclerView recyclerView;

    @InjectView(R.id.btnSubmitComment)
    private ImageButton sendComment;

    @InjectView(R.id.et_comment)
    private EditText commentEditText;

    @Inject
    protected NetworkService networkService;

    private String url;
    private Typeface tf;
    private Profile profile;
    private String comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tf = Constants.solaimanLipiFont(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new AbstractListAdapter<Comment, CommentViewHolder>(comments) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
                return new CommentViewHolder(v);
            }

            @Override
            public void onBindViewHolder(CommentViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("মন্তব্য করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                if (!comments.get(position).getProfileimage().equals("")) {
                    Picasso.with(getContext()).load(comments.get(position).getProfileimage()).into(holder.profileImageView);
                }
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });

        if (getArguments().getInt("number") == 2) {
            url = Constants.FETCH_BLOG_POST_COMMENTS_URL;
        } else {
            url = Constants.FETCH_PLACE_COMMENTS_URL;
        }

        Log.d(Constants.TAG, url);

        networkService.fetchComments(url, getArguments().getInt("id"), new DefaultMessageHandler(getContext()) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                try {
                    JSONObject response = new JSONObject(string);
                    try {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"),jsonObject.getString("profileimage"), jsonObject.getString("timestamp")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    if (comments.size() != 0) {
                        recyclerView.smoothScrollToPosition(comments.size() - 1);
                    }
                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    comment = commentEditText.getText().toString().trim();
                    if (!comment.equals("")) {

                        if (getArguments().getInt("number") == 2) {
                            url = Constants.INSERT_BLOG_POST_COMMENT_URL;
                        } else {
                            url = Constants.INSERT_PLACE_COMMENT_URL;
                        }

                        networkService.insertComment(comment, profile.getName(), url, getArguments().getInt("id"),profile.getProfilePictureUri(50,50).toString(), System.currentTimeMillis() + "", new DefaultMessageHandler(getContext(), true, "Posting comment..Please wait...") {
                            @Override
                            public void onSuccess(Message msg) {

                                Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
                                comments.add(new Comment(profile.getName(), comment, profile.getProfilePictureUri(50,50).toString(), System.currentTimeMillis() + ""));
                                recyclerView.getAdapter().notifyDataSetChanged();
                                if (comments.size() != 0) {
                                    recyclerView.smoothScrollToPosition(comments.size() - 1);
                                }
                                Log.d(Constants.TAG, msg.obj.toString());
                            }
                        });
                        commentEditText.getText().clear();
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView commenter;
        protected TextView comment;
        protected TextView timestamp;
        protected ImageView profileImageView;

        public CommentViewHolder(View v) {
            super(v);
            commenter = ViewHolder.get(v, R.id.tvName);
            comment = ViewHolder.get(v, R.id.tvComment);
            timestamp = ViewHolder.get(itemView, R.id.tv_time_stamp);
            profileImageView = ViewHolder.get(itemView,R.id.profile_image);
        }
    }
}
