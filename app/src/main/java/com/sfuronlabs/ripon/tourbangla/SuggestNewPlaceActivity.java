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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
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
public class SuggestNewPlaceActivity extends AppCompatActivity {
    EditText name, address, division, description, howtogo, hotels;
    Button suggestDone;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestnewplace);

        name = (EditText) findViewById(R.id.etSuggestedPlaceName);
        address = (EditText) findViewById(R.id.etSuggestedPlaceAddress);
        division = (EditText) findViewById(R.id.etSuggestedPlaceDivision);
        description = (EditText) findViewById(R.id.etSuggestedPlaceDescription);
        howtogo = (EditText) findViewById(R.id.etSuggestedPlaceHowtogo);
        hotels = (EditText) findViewById(R.id.etSuggestedPlaceHotels);

        suggestDone = (Button) findViewById(R.id.btnDoneSuggest);

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


        suggestDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String pname = name.getText().toString().trim();
                final String paddress = address.getText().toString().trim();
                final String pdivision = division.getText().toString().trim();
                final String pdescription = description.getText().toString().trim();
                final String phowtogo = howtogo.getText().toString().trim();
                final String photels = hotels.getText().toString().trim();
                if (pname.length() == 0 || paddress.length() == 0 || pdivision.length() == 0 || pdescription.length() == 0 || phowtogo.length() == 0 || photels.length() == 0) {
                    Toast.makeText(SuggestNewPlaceActivity.this, "Please give input correctly", Toast.LENGTH_LONG).show();
                    return;
                }

                name.getText().clear();
                address.getText().clear();
                division.getText().clear();
                description.getText().clear();
                howtogo.getText().clear();
                hotels.getText().clear();

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("name",pname);
                params.put("address",paddress);
                params.put("division",pdivision);
                params.put("description",pdescription);
                params.put("howtogo",phowtogo);
                params.put("hotels",photels);
                params.put("key","bl905577");
                asyncHttpClient.post("http://209.58.178.96/footballstreamlive/SuggestNewPlace.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        String res = bytes.toString();
                        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                        String res = bytes.toString();
                        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Thank you for your suggestion.", Toast.LENGTH_LONG).show();

            }

        });
    }


}
