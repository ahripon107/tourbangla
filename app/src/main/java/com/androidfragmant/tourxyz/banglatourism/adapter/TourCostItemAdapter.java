package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.CostItem;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Ripon on 8/5/16.
 */
public class TourCostItemAdapter extends RecyclerView.Adapter<TourCostItemAdapter.CostItemViewHolder> {

    Context context;
    ArrayList<CostItem> costItems;
    Typeface tf;

    public TourCostItemAdapter(Context context, ArrayList<CostItem> costItems) {
        this.context = context;
        this.costItems = costItems;
        tf = Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }

    @Override
    public CostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sigle_tour_cost_item,parent,false);
        return new CostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CostItemViewHolder holder, int position) {
        holder.costAmount.setTypeface(tf);
        holder.costPurpose.setTypeface(tf);
        holder.costAmount.setText("খরচের পরিমাণ : "+costItems.get(position).getCostAmount()+" টাকা");
        holder.costPurpose.setText("খরচের খাত : "+costItems.get(position).getCostPurpose());
    }

    @Override
    public int getItemCount() {
        return costItems.size();
    }

    static class CostItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView costAmount;
        protected TextView costPurpose;

        public CostItemViewHolder(View itemView) {
            super(itemView);
            costAmount = ViewHolder.get(itemView,R.id.tv_cost_amount);
            costPurpose = ViewHolder.get(itemView,R.id.tv_cost_purpose);
        }
    }
}
