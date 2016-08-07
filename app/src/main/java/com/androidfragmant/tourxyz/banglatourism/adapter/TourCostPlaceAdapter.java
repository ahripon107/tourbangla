package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.TourCostItemActivity;
import com.androidfragmant.tourxyz.banglatourism.model.CostPlace;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/5/16.
 */
public class TourCostPlaceAdapter extends RecyclerView.Adapter<TourCostPlaceAdapter.CostPlaceViewHolder> {

    Context context;
    ArrayList<CostPlace> costPlaces;
    Typeface tf;
    public TourCostPlaceAdapter(Context context,ArrayList<CostPlace> costPlaces) {
        this.context = context;
        this.costPlaces = costPlaces;
        tf = Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }
    @Override
    public CostPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tour_cost_place,parent,false);
        return new CostPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CostPlaceViewHolder holder, final int position) {
        holder.costPlace.setTypeface(tf);
        holder.totallCost.setTypeface(tf);
        holder.costPlace.setText("ট্যুর : "+costPlaces.get(position).getCostPlace());
        holder.totallCost.setText("সর্বমোট খরচ "+costPlaces.get(position).getCost()+" টাকা");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TourCostItemActivity.class);
                intent.putExtra("placeid",costPlaces.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return costPlaces.size();
    }

    static class CostPlaceViewHolder extends RecyclerView.ViewHolder {
        protected TextView costPlace;
        protected TextView totallCost;
        protected LinearLayout linearLayout;
        public CostPlaceViewHolder(View itemView) {
            super(itemView);
            costPlace = ViewHolder.get(itemView,R.id.tv_tour_place);
            totallCost = ViewHolder.get(itemView,R.id.tv_total_cost);
            linearLayout = ViewHolder.get(itemView,R.id.cost_place_layout);
        }
    }
}
