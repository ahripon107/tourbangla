package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.Comment;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.CommentAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 8/27/15.
 */
public class CommentAddComment extends Fragment {
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
    String url;
    RecyclerView recyclerView;

    public CommentAddComment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.commentandaddcomment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);


        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), comments);
        recyclerView.setAdapter(commentAdapter);
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

        FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        comments.add(new Comment(jsonObject.getString("name"),jsonObject.getString("comment"),jsonObject.getString("timestamp")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.notifyDataSetChanged();
                if (comments.size() != 0) {
                    recyclerView.smoothScrollToPosition(comments.size()-1);
                }
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), statusCode + "Failed", Toast.LENGTH_LONG).show();
            }
        });

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
                        if (Validator.validateNotEmpty(yourName,"Required") && Validator.validateNotEmpty(writeComment,"Required")) {
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
                            FetchFromWeb.post(url, params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Comment successfully posted", Toast.LENGTH_LONG).show();
                                    comments.add(new Comment(name,comment,System.currentTimeMillis() + ""));
                                    commentAdapter.notifyDataSetChanged();
                                    if (comments.size() != 0) {
                                        recyclerView.smoothScrollToPosition(comments.size()-1);
                                    }
                                    Log.d(Constants.TAG, response.toString());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        return v;
    }
}
