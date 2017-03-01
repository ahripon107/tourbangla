package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.view.ProgressWheel;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.activity_hotels)
public class HotelsActivity extends RoboAppCompatActivity {

    @InjectView(R.id.pwHotels)
    ProgressWheel progressWheel;

    @InjectView(R.id.lvHotels)
    ListView hotels;
    String[] names;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        progressWheel.spin();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        final String str = i.getExtras().getString("place");
        setTitle("Hotels of "+str);
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

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
