package com.sfuronlabs.ripon.tourbangla;

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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Ripon on 9/21/15.
 */
public class NewTourBlog extends AppCompatActivity {
    EditText title,details,tags,writername;
    Button selectimage,done1;
    File selectedFile;
    private Uri mImageCaptureUri;
    TextView selectedPicture;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtourblog);
        title = (EditText) findViewById(R.id.etBlogTitle);
        details = (EditText) findViewById(R.id.etBlogDetails);
        tags = (EditText) findViewById(R.id.etTags);
        writername = (EditText) findViewById(R.id.etBlogWriterName);
        selectimage = (Button) findViewById(R.id.btnSelectPicture);
        done1 = (Button) findViewById(R.id.btnDone);
        selectedPicture = (TextView) findViewById(R.id.tvPic);
        toolbar = (Toolbar) findViewById(R.id.tool_barnewblog);
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

        selectimage.setOnClickListener(new View.OnClickListener() {
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
                final String blogTitle = title.getText().toString().trim();
                final String blogDetails = details.getText().toString().trim();
                final String blogTags = tags.getText().toString().trim();
                final String blogWriterName = writername.getText().toString().trim();

                if (blogTitle.length()== 0 || blogDetails.length() == 0 || blogTags.length()==0 || blogWriterName.length()==0)
                {
                    Toast.makeText(NewTourBlog.this,"Please give input correctly",Toast.LENGTH_LONG).show();
                    return;
                }

                byte[] bytes;

                if (selectedFile!=null)
                {
                    int size = (int) selectedFile.length();

                    bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(selectedFile));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                else
                {
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.noimage);
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bytes = stream.toByteArray();
                }

                title.getText().clear();
                details.getText().clear();
                tags.getText().clear();
                writername.getText().clear();
                selectedPicture.setText("No Picture Selected");

                final ParseFile file = new ParseFile("pic.jpg",bytes);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject parseObject = new ParseObject("BlogPosts");
                        parseObject.put("blogdetails", blogDetails);
                        parseObject.put("blogtitle", blogTitle);
                        parseObject.put("tags", blogTags);
                        //parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        //parseObject.put("name", (String) ParseUser.getCurrentUser().get("FullName"));
                        parseObject.put("username", "username");
                        parseObject.put("name", blogWriterName);

                        parseObject.put("picture", file);
                        parseObject.saveInBackground();
                    }
                });

                Toast.makeText(NewTourBlog.this,"Your post will be added",Toast.LENGTH_LONG).show();
                //finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            String path     = "";

            switch(requestCode) {

                case 1:

                    mImageCaptureUri = data.getData();
                    path = getRealPathFromURI(mImageCaptureUri); //from Gallery

                    if (path == null)
                        path = mImageCaptureUri.getPath(); //from File Manager
                    selectedFile = new File(path);
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
        if (isKitKat)
        {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":")+1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        else
        {
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
            cursor.moveToFirst();



            int columnIndex = cursor.getColumnIndex(projection[0]);
            path = cursor.getString(columnIndex); // returns null
            cursor.close();
        }

        return path;
    }


}
