package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

/**
 * Created by Ripon on 8/29/15.
 */
public class HotelListStyle extends ArrayAdapter<String> {

    private final Activity context;
    Typeface tf;
    private String[] hotelNames;

    public HotelListStyle(Activity paramActivity, String[] hotelNames) {
        super(paramActivity, R.layout.singlehotel);
        this.context = paramActivity;
        this.hotelNames = hotelNames;
        this.tf = Typeface.createFromAsset(paramActivity.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }

    @Override
    public int getCount() {
        return hotelNames.length;
    }

    @Override
    public View getView(int position, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;
        if (localView == null) {
            localView = this.context.getLayoutInflater().inflate(R.layout.singlehotel, paramViewGroup, false);
        }
        TextView name = (TextView) localView.findViewById(R.id.tvHotelName);
        name.setTypeface(tf);
        name.setText(hotelNames[position]);
        return localView;
    }
}
