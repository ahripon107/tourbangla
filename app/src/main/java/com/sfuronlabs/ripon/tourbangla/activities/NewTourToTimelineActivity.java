package com.sfuronlabs.ripon.tourbangla.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sfuronlabs.ripon.tourbangla.R;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ripon on 9/17/15.
 */
public class NewTourToTimelineActivity extends AppCompatActivity {

    TextView selectedPicture;

    EditText placeName,startDate,endDate;
    Button choosePicture,allDone;
    private static final int REQUEST_PICK_FILE = 1;
    File selectedFile;
    Toolbar mToolbar;
    private Uri mImageCaptureUri;
    Calendar myCalendar;
    int who = 100;
    DatePickerDialog.OnDateSetListener date;
    //DatePickerDialog dateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtourtotimeline);
        mToolbar = (Toolbar) findViewById(R.id.tool_bartourtimeline);
        mToolbar.setTitle("Add New Tour");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //dateDialog = new DatePickerDialog(this,date,Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectedPicture = (TextView) findViewById(R.id.tvSelectedPicture);
        startDate = (EditText) findViewById(R.id.etStartDate);
        endDate = (EditText) findViewById(R.id.etEndDate);
        placeName = (EditText) findViewById(R.id.etPlaceName);

        choosePicture = (Button) findViewById(R.id.btnChoosePicture);
        allDone = (Button) findViewById(R.id.btnDone);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }


        };

        /*if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB){ // Fix for some Samsung/Nexus devices not explicitly calling onDateSet
            dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatePicker datePicker = dateDialog.getDatePicker();
                    //listener.onDateChanged(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
            });
        }*/




        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewTourToTimelineActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                who = 1;
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewTourToTimelineActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                who = 2;
            }
        });



        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }
        });

        allDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseObject parseObject = new ParseObject("Tour");
                String name = placeName.getText().toString().trim();
                String stdt = startDate.getText().toString().trim();
                String endt = endDate.getText().toString().trim();
                if (name.length()== 0 || stdt.length()==0 || endt.length()==0 || selectedFile == null)
                {
                    Toast.makeText(NewTourToTimelineActivity.this,"Please give input correctly",Toast.LENGTH_LONG).show();
                    return;
                }
                parseObject.put("tourPlace",name);
                parseObject.put("startdate",stdt);
                parseObject.put("enddate",endt);
                parseObject.put("userinfo", ParseUser.getCurrentUser());
                parseObject.saveInBackground();

                /*byte[] data = new byte[(int) selectedFile.length()];
                try {
                    data = IOUtil.readFile(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                int size = (int) selectedFile.length();

                byte[] bytes = new byte[size];
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

                placeName.getText().clear();
                startDate.getText().clear();
                endDate.getText().clear();
                selectedPicture.setText("Selected Picture");

                final ParseFile file = new ParseFile("pic.jpg",bytes);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject parseObject1 = new ParseObject("TourPicture");
                        parseObject1.put("relatedtour",parseObject);
                        parseObject1.put("picture",file);
                        parseObject1.put("username", ParseUser.getCurrentUser());
                        parseObject1.saveInBackground();

                    }
                });
                Toast.makeText(NewTourToTimelineActivity.this,"This will be added to your timeline",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateLabel()
    {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (who == 1)
            startDate.setText(sdf.format(myCalendar.getTime()));
        else
            endDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            String path     = "";

            switch(requestCode) {

                case REQUEST_PICK_FILE:

                    mImageCaptureUri = data.getData();
                    path = getRealPathFromURI(mImageCaptureUri); //from Gallery

                    if (path == null)
                        path = mImageCaptureUri.getPath(); //from File Manager
                    selectedFile = new File(path);
                    selectedPicture.setText(selectedFile.getPath());
                    Toast.makeText(getApplicationContext(),""+selectedFile.getAbsolutePath()+" "+selectedFile.length(),Toast.LENGTH_LONG).show();
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
