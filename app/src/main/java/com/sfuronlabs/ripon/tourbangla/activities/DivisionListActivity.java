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

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 7/5/16.
 */
@ContentView(R.layout.divisionlist)
public class DivisionListActivity extends RoboAppCompatActivity {

    @InjectView(R.id.divisionListRecyclerView)
    RecyclerView recyclerView;

    @InjectView(R.id.divisionListBar)
    Toolbar toolbar;

    String[] divisions;
    String[] divisionsBangla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        divisions = getResources().getStringArray(R.array.division_list);
        divisionsBangla = getResources().getStringArray(R.array.division_list_bangla);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Select Division");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DivisionListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        DivisionListRecyclerAdapter adapter = new DivisionListRecyclerAdapter(DivisionListActivity.this,divisions,divisionsBangla);
        recyclerView.setAdapter(adapter);
    }
}

class DivisionListRecyclerAdapter extends RecyclerView.Adapter<DivisionListRecyclerAdapter.DivisionListViewHolder> {
    Context context;
    String[] divisions;
    String[] divisionsBangla;
    Typeface tf;

    DivisionListRecyclerAdapter(Context context,String[] divisions,String[] divisionsBangla) {
        this.context = context;
        this.divisions = divisions;
        this.divisionsBangla = divisionsBangla;
        tf = Typeface.createFromAsset(context.getAssets(),"font/solaimanlipi.ttf");
    }

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
                Intent intent = new Intent(context, DistrictListActivity.class);
                intent.putExtra("divisionName",divisions[holder.getAdapterPosition()]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return divisions.length;
    }

    static class DivisionListViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView2;
        LinearLayout linearLayout;

        public DivisionListViewHolder(View itemView) {
            super(itemView);
            textView = ViewHolder.get(itemView, R.id.text1);
            textView2 = ViewHolder.get(itemView,R.id.text2);
            linearLayout = ViewHolder.get(itemView, R.id.division_list_item_container);
        }
    }
}