package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.ForumPost;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.google.gson.Gson;
import com.google.inject.Inject;

import java.util.ArrayList;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.ForumPostListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.forumpostlist)
public class ForumPostListFragment extends RoboFragment {

    @InjectView(R.id.recyclerViewForumPostList)
    private RecyclerView recyclerView;


    @InjectView(R.id.fabAddNewForumPost)
    private FloatingActionButton fab;


    private ArrayList<ForumPost> forumPosts;

    @Inject
    private NetworkService networkService;

    private ForumPostListAdapter forumPostListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forumPosts = new ArrayList<>();
        forumPostListAdapter = new ForumPostListAdapter(getContext(), forumPosts);

        fetchContents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forumpostlist,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(forumPostListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.addnewforumpost, null, false);
                final EditText writeComment = (EditText) promptsView.findViewById(R.id.etYourQuestion);
                final EditText yourName = (EditText) promptsView.findViewById(R.id.etYourForumPostName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(promptsView);

                builder.setTitle("নতুন ফোরাম পোস্ট").setPositiveButton("SUBMIT", null).setNegativeButton("CANCEL", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validator.validateNotEmpty(yourName, "Required") && Validator.validateNotEmpty(writeComment, "Required")) {

                            networkService.insertForumPost(yourName.getText().toString(), writeComment.getText().toString(), System.currentTimeMillis() + "",
                                    new DefaultMessageHandler(getContext(), true) {
                                        @Override
                                        public void onSuccess(Message msg) {
                                            Toast.makeText(getContext(), "Successfully Posted", Toast.LENGTH_LONG).show();
                                            fetchContents();
                                        }
                                    });

                            alertDialog.dismiss();
                        }
                    }
                });
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
}
