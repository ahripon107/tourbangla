package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentImage;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by Ripon on 7/13/16.
 */
public class SlideShowViewPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<HomeFragmentImage> homeFragmentImages;
    String category;
    Typeface tf;

    public SlideShowViewPagerAdapter(Context context, ArrayList<HomeFragmentImage> homeFragmentImages) {
        this.homeFragmentImages = homeFragmentImages;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        tf = Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
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
            ImageUtil.displayImage(imageView, homeFragmentImages.get(position).getImagename(), null);
        }
        container.addView(itemView);
        tvText.setTypeface(tf);
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
