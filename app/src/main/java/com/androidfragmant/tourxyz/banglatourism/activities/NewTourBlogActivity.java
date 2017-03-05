package com.androidfragmant.tourxyz.banglatourism.activities;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.androidfragmant.tourxyz.banglatourism.util.Utility;
import com.androidfragmant.tourxyz.banglatourism.util.Validator;
import com.facebook.Profile;
import com.google.inject.Inject;
import com.loopj.android.http.Base64;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_new_tour_blog)
public class NewTourBlogActivity extends RoboAppCompatActivity {

    @InjectView(R.id.etBlogTitle)
    private EditText title;

    @InjectView(R.id.etBlogDetails)
    private EditText details;

    @InjectView(R.id.btnDone)
    private Button done;

    @InjectView(R.id.tvPic)
    private TextView selectedPicture;

    @InjectView(R.id.btnSelectPicture)
    private Button selectImage;

    @Inject
    private NetworkService networkService;

    private Bitmap bitmap;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result=Utility.checkPermission(NewTourBlogActivity.this);
                if (result)  {
                    galleryIntent();
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    if (Validator.validateNotEmpty(title, "Required")
                            && Validator.validateNotEmpty(details, "Required")) {

                        final String blogTitle = title.getText().toString().trim();
                        final String blogDetails = details.getText().toString().trim();

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
                        Log.d(Constants.TAG, encodedImage.getBytes().length + "");
                        Log.d(Constants.TAG, bytes.length + "");

                        networkService.insertNewTourBlog(encodedImage, blogTitle, blogDetails, profile.getName(),
                                System.currentTimeMillis() + "", new DefaultMessageHandler(NewTourBlogActivity.this, true) {
                                    @Override
                                    public void onSuccess(Message msg) {
                                        String string = (String) msg.obj;
                                        try {
                                            JSONObject response = new JSONObject(string);
                                            title.getText().clear();
                                            details.getText().clear();
                                            selectedPicture.setText("No Picture Selected");
                                            Toast.makeText(NewTourBlogActivity.this, "Your Post Added Successfully.", Toast.LENGTH_SHORT).show();
                                            EventBus.getDefault().post(new BlogPost(profile.getName(), blogTitle, blogDetails, encodedImage, System.currentTimeMillis() + ""));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                } else {
                    Intent intent = new Intent(NewTourBlogActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {

                }
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String path = "";

            switch (requestCode) {
                case 1:
                    Uri mImageCaptureUri = data.getData();
                    path = getRealPathFromURI(mImageCaptureUri); //from Gallery
                    Log.d(Constants.TAG, mImageCaptureUri.toString());
                    if (path == null)
                        path = mImageCaptureUri.getPath(); //from File Manager
                    File selectedFile = new File(path);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}