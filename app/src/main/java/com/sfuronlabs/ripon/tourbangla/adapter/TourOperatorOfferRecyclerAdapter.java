package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.activities.TourOfferDetailsActivity;
import com.sfuronlabs.ripon.tourbangla.model.TourOperatorOffer;
import com.sfuronlabs.ripon.tourbangla.util.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ripon on 7/6/16.
 */
public class TourOperatorOfferRecyclerAdapter extends RecyclerView.Adapter<TourOperatorOfferRecyclerAdapter.TourOperatorOfferViewHolder> {

    Context context;
    ArrayList<TourOperatorOffer> offers;
    Typeface tf;
    public TourOperatorOfferRecyclerAdapter(Context context, ArrayList<TourOperatorOffer> offers) {
        this.context = context;
        this.offers = offers;
        tf = Typeface.createFromAsset(context.getAssets(), "font/solaimanlipi.ttf");
    }
    @Override
    public TourOperatorOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singletouroperatoroffer,parent,false);
        return new TourOperatorOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TourOperatorOfferViewHolder holder, int position) {
        final TourOperatorOffer offer = offers.get(position);
        holder.offerTitle.setText(offer.getTitle());
        holder.offerTitle.setTypeface(tf);
        holder.offerSummary.setText(offer.getSummary());
        Picasso.with(context).load("http://apisea.xyz/TourBangla/images/" + offers.get(position).getImageName() + ".jpg").into(holder.offerImage);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TourOfferDetailsActivity.class);
                intent.putExtra("details",offer.getDetails());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    static class TourOperatorOfferViewHolder extends RecyclerView.ViewHolder {

        TextView offerTitle;
        TextView offerSummary;
        Button button;
        ImageView offerImage;

        public TourOperatorOfferViewHolder(View itemView) {
            super(itemView);
            offerTitle = ViewHolder.get(itemView, R.id.tourOfferTitle);
            offerSummary = ViewHolder.get(itemView,R.id.tourOfferSummary);
            button = ViewHolder.get(itemView,R.id.tourOfferDetails);
            offerImage = ViewHolder.get(itemView,R.id.tourOfferImage);
        }
    }
}
