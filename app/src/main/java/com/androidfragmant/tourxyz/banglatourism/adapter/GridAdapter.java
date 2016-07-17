package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ripon on 6/14/15.
 */
public class GridAdapter extends BaseAdapter {
    private Activity context;
    private String[] web;
    private String[] picname;

    public GridAdapter(Activity paramActivity, String[] paramArrayOfString,
                       String[] pics, String paramString) {
        this.context = paramActivity;
        this.web = paramArrayOfString;
        this.picname = pics;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View localView = convertView;
        if (localView == null) {
            localView = this.context.getLayoutInflater().inflate(R.layout.griditem, parent, false);

        }
        TextView localTextView = (TextView) localView.findViewById(R.id.textdescription);
        ImageView localImageView = (ImageView) localView.findViewById(R.id.picture);
        localTextView.setText(this.web[position]);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/solaimanlipi.ttf");
        localTextView.setTypeface(tf);
        Picasso.with(context).load("http://apisea.xyz/TourBangla/images/" + this.picname[position] + ".jpg").placeholder(R.drawable.noimage).into(localImageView);

        return localView;
    }
}
