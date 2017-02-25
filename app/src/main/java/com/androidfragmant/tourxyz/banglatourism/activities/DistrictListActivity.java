package com.androidfragmant.tourxyz.banglatourism.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.model.DistrictName;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.inject.Inject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.divisionlist)
public class DistrictListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.divisionListRecyclerView)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<DistrictName> districtNames;

    private String[] districts;
    private String[] districtsBangla;
    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tf = Constants.solaimanLipiFont(this);

        final String divisionName = getIntent().getStringExtra(Constants.DIVISION_NAME);

        switch (divisionName) {
            case "Dhaka":
                districts = getResources().getStringArray(R.array.dhaka_districts);
                districtsBangla = getResources().getStringArray(R.array.dhaka_districts_bangla);
                break;
            case "Chittagong":
                districts = getResources().getStringArray(R.array.chittagong_districts);
                districtsBangla = getResources().getStringArray(R.array.chittagong_districts_bangla);
                break;
            case "Rajshahi":
                districts = getResources().getStringArray(R.array.rajshahi_districts);
                districtsBangla = getResources().getStringArray(R.array.rajshahi_districts_bangla);
                break;
            case "Rangpur":
                districts = getResources().getStringArray(R.array.rangpur_districts);
                districtsBangla = getResources().getStringArray(R.array.rangpur_districts_bangla);
                break;
            case "Barisal":
                districts = getResources().getStringArray(R.array.barisal_districts);
                districtsBangla = getResources().getStringArray(R.array.barisal_districts_bangla);
                break;
            case "Mymensingh":
                districts = getResources().getStringArray(R.array.mymensingh_districts);
                districtsBangla = getResources().getStringArray(R.array.mymensingh_districts_bangla);
                break;
            case "Sylhet":
                districts = getResources().getStringArray(R.array.sylhet_districts);
                districtsBangla = getResources().getStringArray(R.array.sylhet_districts_bangla);
                break;
            case "Khulna":
                districts = getResources().getStringArray(R.array.khulna_districts);
                districtsBangla = getResources().getStringArray(R.array.khulna_districts_bangla);
                break;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Select District");

        for (int i=0;i<districts.length;i++) {
            districtNames.add(new DistrictName(districts[i],districtsBangla[i]));
        }


        recyclerView.setAdapter(new AbstractListAdapter<DistrictName,DistrictListViewHolder>(districtNames) {
            @Override
            public DistrictListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.division_list_item, parent, false);
                return new DistrictListViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final DistrictListViewHolder holder, int position) {
                holder.textView1.setTypeface(tf);
                holder.textView1.setText(districtsBangla[holder.getAdapterPosition()]);
                holder.textView2.setText(districts[position]);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DistrictListActivity.this, BrowseByDivisionActivity.class);
                        intent.putExtra(Constants.DIVISION_NAME, divisionName);
                        intent.putExtra(Constants.DISTRICT_NAME, districts[holder.getAdapterPosition()]);
                        startActivity(intent);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private static class DistrictListViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView1, textView2;
        protected LinearLayout linearLayout;

        public DistrictListViewHolder(View itemView) {
            super(itemView);
            textView1 = ViewHolder.get(itemView, R.id.text1);
            textView2 = ViewHolder.get(itemView, R.id.text2);
            linearLayout = ViewHolder.get(itemView, R.id.division_list_item_container);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
