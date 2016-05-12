package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfuronlabs.ripon.tourbangla.R;
import com.sfuronlabs.ripon.tourbangla.model.DummyModel;
import com.sfuronlabs.ripon.tourbangla.util.ImageUtil;

import java.util.List;

/**
 * Created by Ripon on 10/1/15.
 */
public class GoogleCardsTravelAdapter extends ArrayAdapter<DummyModel>
        implements View.OnClickListener {
    private LayoutInflater mInflater;
    int position1;
    Context con;

    public GoogleCardsTravelAdapter(Context context, List<DummyModel> items) {
        super(context, 0, items);
        con = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        position1 = position;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_item_google_cards_travel, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_image);
            //holder.categoryName = (TextView) convertView
              //      .findViewById(R.id.list_item_google_cards_travel_category_name);
            holder.title = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_title);
            //holder.explore = (TextView) convertView
              //      .findViewById(R.id.list_item_google_cards_travel_explore);
            //holder.explore.setOnClickListener(this);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DummyModel item = getItem(position);
        ImageUtil.displayImage(holder.image, item.getImageURL(), null);
        holder.title.setText(item.getText());
        //holder.explore.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;
        //public TextView categoryName;
        public TextView title;
        public TextView text;
        //public TextView explore;

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int possition = (Integer) v.getTag();
        switch (v.getId()) {
            /*case R.id.list_item_google_cards_travel_explore:
                // click on explore button
                Intent i = new Intent("android.intent.action.NEWPLACEDETAILSACTIVITY");
                i.putExtra("index", position1);
                con.startActivity(i);
                break;*/

        }
    }
}
