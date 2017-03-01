package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

/**
 * @author Ripon
 */
public class HotelListStyle extends ArrayAdapter<String> {

    private Activity context;
    private Typeface tf;
    private String[] hotelNames;

    public HotelListStyle(Activity paramActivity, String[] hotelNames) {
        super(paramActivity, R.layout.list_item_hotel);
        this.context = paramActivity;
        this.hotelNames = hotelNames;
        this.tf = Constants.solaimanLipiFont(paramActivity);
    }

    @Override
    public int getCount() {
        return hotelNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;
        if (localView == null) {
            localView = this.context.getLayoutInflater().inflate(R.layout.list_item_hotel, paramViewGroup, false);
        }
        TextView name = (TextView) localView.findViewById(R.id.tvHotelName);
        name.setTypeface(tf);
        name.setText(hotelNames[position]);
        return localView;
    }
}
