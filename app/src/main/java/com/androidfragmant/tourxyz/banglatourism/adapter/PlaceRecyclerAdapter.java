package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.NewPlaceDetailsActivity;
import com.androidfragmant.tourxyz.banglatourism.model.Place;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ImageUtil;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import java.util.List;

/**
 * Created by Ripon on 10/1/15.
 */
public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.PlaceCardViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    List<Place> items;
    Typeface tf;

    public PlaceRecyclerAdapter(Context context, List<Place> items) {
        this.context = context;
        this.items = items;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tf = Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }

    @Override
    public PlaceCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_place,parent,false);
        return new PlaceCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceCardViewHolder holder, int position) {
        final Place place = items.get(holder.getAdapterPosition());
        holder.title.setTypeface(tf);
        ImageUtil.displayImage(holder.imageView, place.getPicture(), null);
        holder.title.setText(place.getName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NewPlaceDetailsActivity.class);
                i.putExtra("id", place.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PlaceCardViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected LinearLayout linearLayout;

        public PlaceCardViewHolder(View itemView) {
            super(itemView);
            imageView = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_image);
            title = ViewHolder.get(itemView,R.id.list_item_google_cards_travel_title);
            linearLayout = ViewHolder.get(itemView,R.id.linear);
        }
    }
}
