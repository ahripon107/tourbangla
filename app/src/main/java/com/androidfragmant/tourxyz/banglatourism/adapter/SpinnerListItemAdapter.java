package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;

import java.util.ArrayList;

/**
 * Created by amin on 8/3/16.
 */
public class SpinnerListItemAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> data;
    LayoutInflater inflater;

    public SpinnerListItemAdapter(Activity context, ArrayList<String> objects) {
        super(context, R.layout.spinner_list_item, objects);

        this.context = context;
        this.data = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.spinner_list_item, parent, false);
        }
        TextView textView = (TextView) row.findViewById(R.id.spnListItemText);
        textView.setBackgroundColor(Color.BLUE);
        textView.setText(data.get(position));
        textView.setTextColor(Color.GREEN);
        return row;
    }
}