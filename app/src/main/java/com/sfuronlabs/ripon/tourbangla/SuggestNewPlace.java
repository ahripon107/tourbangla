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

import com.parse.ParseAnonymousUtils;
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
 * Created by Ripon on 9/27/15.
 */
public class SuggestNewPlace extends AppCompatActivity {
    EditText name,address,division,description,howtogo,hotels,yname;
    Button suggestImageSelect,suggestDone;
    File selectedFile;
    private Uri mImageCaptureUri;
    TextView selectedPicture;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestnewplace);
        yname = (EditText) findViewById(R.id.etName);
        name = (EditText) findViewById(R.id.etSuggestedPlaceName);
        address = (EditText) findViewById(R.id.etSuggestedPlaceAddress);
        division = (EditText) findViewById(R.id.etSuggestedPlaceDivision);
        description = (EditText) findViewById(R.id.etSuggestedPlaceDescription);
        howtogo = (EditText) findViewById(R.id.etSuggestedPlaceHowtogo);
        hotels = (EditText) findViewById(R.id.etSuggestedPlaceHotels);
        suggestImageSelect = (Button) findViewById(R.id.btnSelectPictureSuggest);
        suggestDone = (Button) findViewById(R.id.btnDoneSuggest);
        selectedPicture = (TextView) findViewById(R.id.tvPicSuggest);
        toolbar = (Toolbar) findViewById(R.id.tool_barsuggestplace);
        setSupportActionBar(toolbar);
        setTitle("Suggest New Place");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        suggestImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        suggestDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        final String yourname = yname.getText().toString().trim();
                        final String pname = name.getText().toString().trim();
                        final String paddress = address.getText().toString().trim();
                        final String pdivision = division.getText().toString().trim();
                        final String pdescription = description.getText().toString().trim();
                        final String phowtogo = howtogo.getText().toString().trim();
                        final String photels = hotels.getText().toString().trim();
                        if (pname.length()==0 || paddress.length()==0 || pdivision.length()==0||pdescription.length()==0||phowtogo.length()==0||photels.length()==0 || yourname.length()==0)
                        {
                            Toast.makeText(SuggestNewPlace.this,"Please give input correctly",Toast.LENGTH_LONG).show();
                            return;
                        }
                        byte[] bytes;
                        name.getText().clear();
                        address.getText().clear();
                        division.getText().clear();
                        description.getText().clear();
                        howtogo.getText().clear();
                        hotels.getText().clear();

                        yname.getText().clear();

                        selectedPicture.setText("No Picture Selected");
                        Toast.makeText(getApplicationContext(),"Thank you for your suggestion.",Toast.LENGTH_LONG).show();
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


                        final ParseFile file = new ParseFile("pic.jpg",bytes);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                ParseObject parseObject = new ParseObject("SuggestedPlaces");
                                parseObject.put("placename",pname);
                                parseObject.put("address",paddress);
                                parseObject.put("division",pdivision);
                                parseObject.put("description",pdescription);
                                parseObject.put("howtogo",phowtogo);
                                parseObject.put("hotels",photels);
                                parseObject.put("username", "username");
                                parseObject.put("name",yourname);
                                parseObject.put("picture",file);
                                parseObject.saveInBackground();
                            }
                        });
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
