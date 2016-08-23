package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAddComment extends Fragment {
    ArrayList<Comment> comments;
    String url;
    RecyclerView recyclerView;
    Typeface tf;

    public CommentAddComment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.commentandaddcomment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);

        tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        comments = new ArrayList<>();

        recyclerView.setAdapter(new AbstractListAdapter<Comment, CommentViewHolder>(comments) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecomment, parent, false);
                return new CommentViewHolder(v);
            }

            @Override
            public void onBindViewHolder(CommentViewHolder holder, int position) {
                holder.comment.setTypeface(tf);
                holder.commenter.setTypeface(tf);
                holder.commenter.setText("মন্তব্য করেছেন:  " + comments.get(position).getName());
                holder.comment.setText(comments.get(position).getComment());
                holder.timestamp.setText(Constants.getTimeAgo(Long.parseLong(comments.get(position).getTimestamp())));
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RequestParams requestParams = new RequestParams();
        if (getArguments().get("number") == 2) {
            requestParams.add(Constants.KEY, Constants.KEY_VALUE);
            requestParams.add("id", getArguments().getInt("id") + "");
            url = Constants.FETCH_BLOG_POST_COMMENTS_URL;
        } else {
            requestParams.add(Constants.KEY, Constants.KEY_VALUE);
            requestParams.add("id", getArguments().getInt("id") + "");
            url = Constants.FETCH_PLACE_COMMENTS_URL;
        }
        Log.d(Constants.TAG, url);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constants.SUCCESS) {
                    JSONObject response = (JSONObject) msg.obj;
                    if (response != null) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                comments.add(new Comment(jsonObject.getString("name"), jsonObject.getString("comment"), jsonObject.getString("timestamp")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        if (comments.size() != 0) {
                            recyclerView.smoothScrollToPosition(comments.size() - 1);
                        }
                        Log.d(Constants.TAG, response.toString());
                    }
                }
            }
        };

        FetchFromWeb fetchFromWeb = new FetchFromWeb(handler);
        fetchFromWeb.retreiveData(url, requestParams);

        Button sendComment = (Button) v.findViewById(R.id.btnSubmitComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.addnewcomment, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourComment);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptsView);
                builder.setTitle("মন্তব্য").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {
                            final String comment = writeComment.getText().toString().trim();
                            final String name = yourName.getText().toString().trim();
                            RequestParams params = new RequestParams();
                            if (getArguments().get("number") == 2) {
                                params.put(Constants.KEY, Constants.KEY_VALUE);
                                params.put("id", getArguments().getInt("id"));
                                params.put("name", name);
                                params.put("comment", comment);
                                params.put("timestamp", System.currentTimeMillis() + "");
                                url = Constants.INSERT_BLOG_POST_COMMENT_URL;
                            } else {
                                params.put(Constants.KEY, Constants.KEY_VALUE);
                                params.put("id", getArguments().getInt("id"));
                                params.put("name", name);
                                params.put("comment", comment);
                                params.put("timestamp", System.currentTimeMillis() + "");
                                url = Constants.INSERT_PLACE_COMMENT_URL;
                            }
                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Posting comment..Please wait...");
                            progressDialog.show();

                            Handler handler1 = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message msg) {
                                    progressDialog.dismiss();
                                    if (msg.what == Constants.SUCCESS) {
                                        JSONObject response = (JSONObject) msg.obj;
                                        if (response != null) {
                                            Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
                                            comments.add(new Comment(name, comment, System.currentTimeMillis() + ""));
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                            if (comments.size() != 0) {
                                                recyclerView.smoothScrollToPosition(comments.size() - 1);
                                            }
                                            Log.d(Constants.TAG, response.toString());
                                        } else {
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            };

                            FetchFromWeb fetchFromWeb1 = new FetchFromWeb(handler1);
                            fetchFromWeb1.postData(url, params);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        return v;
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
