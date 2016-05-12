package com.sfuronlabs.ripon.tourbangla;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ripon on 9/20/15.
 */
public class TourAllPhotos extends AppCompatActivity {
    ViewPager viewPager;
    ImageSlideshowAdapter adapter;
    ArrayList<byte[]> pics;
    Button deleteTour,addPic;
    private Uri mImageCaptureUri;
    File selectedFile;
    int index;
    Toolbar toolbar;
    ArrayList<ParseObject> allPictures;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourallphotos);
        allPictures = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.pagerforimageslideshow);
        deleteTour = (Button) findViewById(R.id.btnDeleteTour);
        //deletePic = (Button) findViewById(R.id.btnDeletePicture);
        addPic = (Button) findViewById(R.id.btnAddPicture);
        toolbar = (Toolbar) findViewById(R.id.tool_barallphotos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Tour Timeline");
        Intent i = getIntent();
        index = i.getExtras().getInt("index");
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        /*deletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),viewPager.getCurrentItem()+"This picture will be removed",Toast.LENGTH_LONG).show();
                pics.remove(pics.size()-1-viewPager.getCurrentItem());
                ParseObject p = allPictures.get(allPictures.size()-1-viewPager.getCurrentItem());
                p.deleteInBackground();
                finish();
                //adapter = new ImageSlideshowAdapter(getSupportFragmentManager(),pics);
                //adapter.notifyDataSetChanged();
                //viewPager.setAdapter(adapter);
            }
        });*/

        deleteTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject parseObject = FriendsFragment.allTours.get(index);
                parseObject.deleteInBackground();
                FriendsFragment.allTours.remove(index);
                for (int c=0;c<allPictures.size();c++)
                {
                    ParseObject p = allPictures.get(c);
                    p.deleteInBackground();
                }
                finish();
            }
        });
        pics = new ArrayList<>();
        adapter = new ImageSlideshowAdapter(getSupportFragmentManager(),pics);
        viewPager.setAdapter(adapter);
        Toast.makeText(TourAllPhotos.this,"Your images are loading, please wait",Toast.LENGTH_LONG).show();
        ParseQuery<ParseObject> parseObjectParseQuery = ParseQuery.getQuery("TourPicture");
        parseObjectParseQuery.whereEqualTo("relatedtour",FriendsFragment.allTours.get(index));
        parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e==null)
                {
                    allPictures = (ArrayList)list;
                    for (int x=0;x<list.size();x++)
                    {
                        ParseFile file = (ParseFile)list.get(x).get("picture");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                pics.add(bytes);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(TourAllPhotos.this,"Error occured",Toast.LENGTH_LONG).show();
                }

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

                    Toast.makeText(getApplicationContext(),"Your image is uploading, please wait...",Toast.LENGTH_LONG).show();
                    int size = (int) selectedFile.length();

                    byte[] bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(selectedFile));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                        pics.add(bytes);
                        adapter.notifyDataSetChanged();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final ParseFile file = new ParseFile("pic.jpg",bytes);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseObject parseObject1 = new ParseObject("TourPicture");
                            parseObject1.put("relatedtour", FriendsFragment.allTours.get(index));
                            parseObject1.put("picture", file);
                            parseObject1.put("username", ParseUser.getCurrentUser());
                            parseObject1.saveInBackground();

                        }
                    });

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
