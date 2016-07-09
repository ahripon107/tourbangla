package com.sfuronlabs.ripon.tourbangla.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.RoboAppCompatActivity;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 7/5/16.
 */
@ContentView(R.layout.divisionlist)
public class DistrictListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.divisionListRecyclerView)
    RecyclerView recyclerView;

    @InjectView(R.id.divisionListBar)
    Toolbar toolbar;

    String[] districts;
    String[] districtsBangla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        String divisionName = intent.getStringExtra("divisionName");

        switch (divisionName) {
            case "Dhaka" :
                districts = getResources().getStringArray(R.array.dhaka_districts);
                districtsBangla = getResources().getStringArray(R.array.dhaka_districts_bangla);
                break;
            case "Chittagong" :
                districts = getResources().getStringArray(R.array.chittagong_districts);
                districtsBangla = getResources().getStringArray(R.array.chittagong_districts_bangla);
                break;
            case "Rajshahi" :
                districts = getResources().getStringArray(R.array.rajshahi_districts);
                districtsBangla = getResources().getStringArray(R.array.rajshahi_districts_bangla);
                break;
            case "Rangpur" :
                districts = getResources().getStringArray(R.array.rangpur_districts);
                districtsBangla = getResources().getStringArray(R.array.rangpur_districts_bangla);
                break;
            case "Barisal" :
                districts = getResources().getStringArray(R.array.barisal_districts);
                districtsBangla = getResources().getStringArray(R.array.barisal_districts_bangla);
                break;
            case "Mymensingh" :
                districts = getResources().getStringArray(R.array.mymensingh_districts);
                districtsBangla = getResources().getStringArray(R.array.mymensingh_districts_bangla);
                break;
            case "Sylhet" :
                districts = getResources().getStringArray(R.array.sylhet_districts);
                districtsBangla = getResources().getStringArray(R.array.sylhet_districts_bangla);
                break;
            case "Khulna" :
                districts = getResources().getStringArray(R.array.khulna_districts);
                districtsBangla = getResources().getStringArray(R.array.khulna_districts_bangla);
                break;

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Select District");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DistrictListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        DistrictListRecyclerAdapter adapter = new DistrictListRecyclerAdapter(DistrictListActivity.this,districts,districtsBangla,divisionName);
        recyclerView.setAdapter(adapter);
    }
}

class DistrictListRecyclerAdapter extends RecyclerView.Adapter<DistrictListRecyclerAdapter.DistrictListViewHolder> {
    Context context;
    String[] districts,districtsBangla;
    String divisionName;
    Typeface tf;

    DistrictListRecyclerAdapter(Context context,String[] districts,String[] districtsBangla,String divisionName) {
        this.context = context;
        this.districts = districts;
        this.districtsBangla = districtsBangla;
        this.divisionName = divisionName;
        tf = Typeface.createFromAsset(context.getAssets(), "font/solaimanlipi.ttf");
    }

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
                Intent intent = new Intent(context, BrowseByDivisionActivity.class);
                intent.putExtra("divisionName",divisionName);
                intent.putExtra("districtName",districts[holder.getAdapterPosition()]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return districts.length;
    }

    static class DistrictListViewHolder extends RecyclerView.ViewHolder {

        TextView textView1,textView2;
        LinearLayout linearLayout;

        public DistrictListViewHolder(View itemView) {
            super(itemView);
            textView1 = ViewHolder.get(itemView, R.id.text1);
            textView2 = ViewHolder.get(itemView,R.id.text2);
            linearLayout = ViewHolder.get(itemView, R.id.division_list_item_container);
        }
    }

}
