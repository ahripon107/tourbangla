package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

                Toast.makeText(NewTourBlogActivity.this, "Your post will be added", Toast.LENGTH_LONG).show();;
            }
        });
    }

}
