package com.sfuronlabs.ripon.tourbangla.activities;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sfuronlabs.ripon.tourbangla.FetchFromWeb;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.util.Constants;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 9/21/15.
 */
@ContentView(R.layout.newtourblog)
public class NewTourBlogActivity extends RoboAppCompatActivity {

    @InjectView(R.id.etBlogTitle)
    EditText title;

    @InjectView(R.id.etBlogDetails)
    EditText details;

    @InjectView(R.id.etTags)
    EditText tags;

    @InjectView(R.id.etBlogWriterName)
    EditText writername;

    @InjectView(R.id.btnDone)
    Button done1;

    @InjectView(R.id.tool_barnewblog)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        setTitle("New Tour Blog");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        done1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blogTitle = title.getText().toString().trim();
                String blogDetails = details.getText().toString().trim();
                String blogTags = tags.getText().toString().trim();
                String blogWriterName = writername.getText().toString().trim();

                if (blogTitle.length() == 0 || blogDetails.length() == 0 || blogTags.length() == 0 || blogWriterName.length() == 0) {
                    Toast.makeText(NewTourBlogActivity.this, "Please give input correctly", Toast.LENGTH_LONG).show();
                    return;
                }

                title.getText().clear();
                details.getText().clear();
                tags.getText().clear();
                writername.getText().clear();

                String url = "http://apisea.xyz/TourBangla/InsertBlogPost.php?key=bl905577&title=" + blogTitle + "&details=" + blogDetails + "&tags=" + blogTags + "&name=" + blogWriterName;
                Log.d(Constants.TAG, url);

                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(NewTourBlogActivity.this,"success",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(NewTourBlogActivity.this,"failure",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}