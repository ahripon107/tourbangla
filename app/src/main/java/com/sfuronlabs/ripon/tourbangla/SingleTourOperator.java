package com.sfuronlabs.ripon.tourbangla;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ripon on 8/28/15.
 */
public class SingleTourOperator extends BaseAdapter {

    private Activity context;
    private String[] web;
    Typeface tf;

    public SingleTourOperator(Activity paramActivity,String[] paramArrayOfString)
    {
        //super(paramActivity, R.layout.griditem, paramArrayOfString);
        this.context = paramActivity;
        this.web = paramArrayOfString;
        this.tf = Typeface.createFromAsset(paramActivity.getAssets(),
                "font/solaimanlipi.ttf");
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
        if (localView == null)
        {
            localView = this.context.getLayoutInflater().inflate(R.layout.singletouroperator, null, true);

        }
        TextView localTextView = (TextView) localView.findViewById(R.id.tvSingleTourOperator);
        localTextView.setTypeface(tf);
        localTextView.setText(this.web[position]);
        //localTextView.setTextColor(Color.GREEN);

        return localView;
    }
}
