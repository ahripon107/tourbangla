package com.androidfragmant.tourxyz.banglatourism.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.loopj.android.http.Base64;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    @InjectView(R.id.tvPic)
    TextView selectedPicture;

    @InjectView(R.id.btnSelectPicture)
    Button selectImage;

    File selectedFile;
    private Uri mImageCaptureUri;
    Bitmap bitmap;

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

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        done1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateNotEmpty(writername,"Required") && Validator.validateNotEmpty(title,"Required")
                        && Validator.validateNotEmpty(details,"Required") && Validator.validateNotEmpty(tags,"Required")) {

                    final String blogTitle = title.getText().toString().trim();
                    final String blogDetails = details.getText().toString().trim();
                    final String blogTags = tags.getText().toString().trim();
                    final String blogWriterName = writername.getText().toString().trim();

                    byte[] bytes;

                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        bytes = stream.toByteArray();
                    } else {
                        Drawable drawable = getResources().getDrawable(R.drawable.noimage);
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        bytes = stream.toByteArray();
                    }
                    final String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                    Log.d(Constants.TAG, encodedImage.getBytes().length+"");
                    Log.d(Constants.TAG, bytes.length+"");

                    RequestParams params = new RequestParams();

                    params.put(Constants.KEY,Constants.KEY_VALUE);
                    params.put("image", encodedImage);
                    params.put("title",blogTitle);
                    params.put("details",blogDetails);
                    params.put("tags",blogTags);
                    params.put("name",blogWriterName);
                    params.put("timestamp",System.currentTimeMillis()+"");

                    final ProgressDialog progressDialog = new ProgressDialog(NewTourBlogActivity.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Handler handler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            progressDialog.dismiss();
                            if (msg.what== Constants.SUCCESS) {
                                JSONObject response = (JSONObject) msg.obj;
                                if (response!=null) {
                                    title.getText().clear();
                                    details.getText().clear();
                                    tags.getText().clear();
                                    writername.getText().clear();
                                    selectedPicture.setText("No Picture Selected");
                                    Toast.makeText(NewTourBlogActivity.this,"Your Post Added Successfully.",Toast.LENGTH_SHORT).show();
                                    EventBus.getDefault().post(new BlogPost(blogWriterName,blogTitle,blogDetails,blogTags,encodedImage,System.currentTimeMillis()+""));
                                } else {
                                    Toast.makeText(NewTourBlogActivity.this,"Failed..Please try again..",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NewTourBlogActivity.this,"Failed..Please try again..",Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    FetchFromWeb fetchFromWeb = new FetchFromWeb(handler);
                    fetchFromWeb.postData(Constants.INSERT_BLOG_POST_URL,params);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String path = "";

            switch (requestCode) {

                case 1:

                    mImageCaptureUri = data.getData();
                    path = getRealPathFromURI(mImageCaptureUri); //from Gallery
                    Log.d(Constants.TAG, mImageCaptureUri.toString());
                    if (path == null)
                        path = mImageCaptureUri.getPath(); //from File Manager
                    selectedFile = new File(path);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectedPicture.setText(selectedFile.getPath());
                    break;
                default:
                    break;
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path;
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
            cursor.moveToFirst();


            int columnIndex = cursor.getColumnIndex(projection[0]);
            path = cursor.getString(columnIndex); // returns null
            cursor.close();
        }

        return path;
    }

}