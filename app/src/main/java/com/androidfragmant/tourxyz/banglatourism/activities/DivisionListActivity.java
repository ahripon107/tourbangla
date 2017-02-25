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
import com.androidfragmant.tourxyz.banglatourism.model.DivisionName;
import com.androidfragmant.tourxyz.banglatourism.util.AbstractListAdapter;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;
import com.google.inject.Inject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.divisionlist)
public class DivisionListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.divisionListRecyclerView)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<DivisionName> divisionNames;

    @InjectResource(R.array.division_list)
    private String[] divisions;

    @InjectResource(R.array.division_list_bangla)
    private String[] divisionsBangla;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tf = Constants.solaimanLipiFont(this);

        for (int i=0;i<divisions.length;i++) {
            divisionNames.add(new DivisionName(divisions[i],divisionsBangla[i]));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(new AbstractListAdapter<DivisionName,DivisionListViewHolder>(divisionNames) {
            @Override
            public DivisionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.division_list_item, parent, false);
                return new DivisionListViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final DivisionListViewHolder holder, int position) {
                holder.textView.setTypeface(tf);
                holder.textView.setText(divisionsBangla[holder.getAdapterPosition()]);
                holder.textView2.setText(divisions[holder.getAdapterPosition()]);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DivisionListActivity.this, DistrictListActivity.class);
                        intent.putExtra(Constants.DIVISION_NAME, divisions[holder.getAdapterPosition()]);
                        startActivity(intent);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(DivisionListActivity.this));
    }

    private static class DivisionListViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;
        protected TextView textView2;
        protected LinearLayout linearLayout;

        public DivisionListViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView, R.id.text1);
            textView2 = ViewHolder.get(itemView, R.id.text2);
            linearLayout = ViewHolder.get(itemView, R.id.division_list_item_container);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}