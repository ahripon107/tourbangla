package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.adapter.GridAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.Place;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ripon
 */
public class VisitedPlacesActivity extends AppCompatActivity {
    ListView listView;
    String[] web = new String[0];
    String[] picname = new String[0];
    ArrayList<Place> allPlaces;
    GridAdapter gridAdapter;


    @Override
    public void onResume() {
        super.onResume();
        allPlaces = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("rating", Context.MODE_PRIVATE);
        Map<String, ?> elements = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            allPlaces.add(place);
        }
        if (allPlaces != null) {
            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];
            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
        }
        gridAdapter = new GridAdapter(this,
                web, picname);
        listView.setAdapter(gridAdapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listview);

        allPlaces = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("rating", Context.MODE_PRIVATE);
        Map<String, ?> elements = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : elements.entrySet()) {
            String string = entry.getValue().toString();
            Gson gson = new Gson();
            Place place = gson.fromJson(string, Place.class);
            allPlaces.add(place);
        }
        if (allPlaces != null) {

            web = new String[allPlaces.size()];
            picname = new String[allPlaces.size()];

            for (int j = 0; j < allPlaces.size(); j++) {
                web[j] = allPlaces.get(j).getName();
                picname[j] = allPlaces.get(j).getPicture();
            }
        }

        gridAdapter = new GridAdapter(this,
                web, picname);
        listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent i = new Intent(VisitedPlacesActivity.this, NewPlaceDetailsActivity.class);
                i.putExtra("id", allPlaces.get(position).getId());
                startActivity(i);
            }
        });
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
