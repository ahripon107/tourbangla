package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ripon on 7/13/16.
 */
public class SlideShowViewPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<HomeFragmentImage> homeFragmentImages;
    String category;


    public SlideShowViewPagerAdapter(Context context,ArrayList<HomeFragmentImage> homeFragmentImages) {
        this.homeFragmentImages = homeFragmentImages;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return homeFragmentImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.imageslideshowelement, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgSlideShow);
        TextView tvText = (TextView) itemView.findViewById(R.id.tvText);
        if (this.category.equals(homeFragmentImages.get(position).getCategory())) {
            Picasso.with(mContext).load(homeFragmentImages.get(position).getImagename()).resize(350, 250).into(imageView);
        }
        container.addView(itemView);

        tvText.setText(homeFragmentImages.get(position).getText());
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void setImageCategoty(String category) {
        this.category = category;
    }

}
