package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ripon on 8/29/15.
 */
public class HotelListStyle extends ArrayAdapter<String> {

    private final Activity context;

    Typeface tf;
    private  String[] hotelNames;

    public HotelListStyle(Activity paramActivity,
                            String[] hotelNames) {
        super(paramActivity, R.layout.singlehotel);
        this.context = paramActivity;
        this.hotelNames = hotelNames;

        this.tf = Typeface.createFromAsset(paramActivity.getAssets(),
                "font/solaimanlipi.ttf");
    }

    @Override
    public int getCount() {
        return hotelNames.length;
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;
        if (localView == null)
        {
            localView = this.context.getLayoutInflater().inflate(R.layout.singlehotel, null, true);
        }

        TextView name = (TextView) localView.findViewById(R.id.tvHotelName);
        name.setTypeface(this.tf, 1);


        name.setText(this.hotelNames[paramInt]);
        return localView;
    }
}
