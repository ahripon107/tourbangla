package com.sfuronlabs.ripon.tourbangla.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sfuronlabs.ripon.tourbangla.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Ripon on 9/20/15.
 */
public class ImageSlideshowAdapter extends FragmentPagerAdapter {


    public ArrayList<byte[]> pics;
    Context context;
    LayoutInflater inflater;
    public static int imageNo;

    public ImageSlideshowAdapter(FragmentManager fragmentManager,ArrayList<byte[]> pics)
    {
        super(fragmentManager);
        this.pics = pics;
        imageNo = 0;
    }


    @Override
    public int getCount() {
        return pics.size();
    }

    @Override
    public Fragment getItem(int position) {
        SwipeFragment fragment = new SwipeFragment();
        imageNo = position;
        return SwipeFragment.newInstance(position,pics);
    }



    public static class SwipeFragment extends Fragment {
        //ArrayList<Bitmap> bitmaps = new ArrayList<>();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.singleimage, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageViewSingle);
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            //if (true)

                byte[] arr = bundle.getByteArray("resId");
                Bitmap d = BitmapFactory.decodeByteArray(arr, 0, arr.length);
                //bitmaps.add(bitmap);
            int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);

            imageView.setImageBitmap(scaled);
            return swipeView;
        }

        static SwipeFragment newInstance(int position,ArrayList<byte[]> resources) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);

            byte[] bmp = resources.get(position);
            bundle.putByteArray("resId",bmp);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }
}
