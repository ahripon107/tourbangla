package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.androidfragmant.tourxyz.banglatourism.R;

import java.util.List;

/**
 * Created by Ripon on 8/21/15.
 */
public class TourBlogListStyle extends ArrayAdapter<ParseObject> {
    private final Activity context;
    private final List<ParseObject> objects;
    Typeface tf;


    public TourBlogListStyle(Activity paramActivity,
                             List<ParseObject> objects,
                             String paramString)
    {
        super(paramActivity, R.layout.tourblogsingleitem, objects);
        this.context = paramActivity;
        this.objects = objects;
        this.tf = Typeface.createFromAsset(paramActivity.getAssets(),
                paramString);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        View localView = paramView;

        if (localView == null)
        {
            localView = this.context.getLayoutInflater().inflate(R.layout.tourblogsingleitem, null, true);
            TextView postTitle = (TextView) localView.findViewById(R.id.txtPostTitle);
            TextView postWriter = (TextView) localView.findViewById(R.id.txtPostWriter);
            TextView postTags = (TextView) localView.findViewById(R.id.txtPostTags);
            postTitle.setTypeface(this.tf, 1);
            postWriter.setTypeface(this.tf,1);
            postTags.setTypeface(this.tf,1);
            final ImageView localImageView = (ImageView) localView.findViewById(R.id.imgBlogPost);
            postTitle.setText((String)this.objects.get(paramInt).get("blogtitle"));
            //localTextView.setTextColor(Color.BLUE);
            postWriter.setText("Posted by: "+(String)this.objects.get(paramInt).get("name"));

            postTags.setText("Tags: "+ (String)this.objects.get(paramInt).get("tags"));
            ParseFile parseFile = (ParseFile)this.objects.get(paramInt).get("picture");
            parseFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e==null)
                    {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        localImageView.setImageBitmap(bitmap);
                    }
                }
            });
            //localImageView.setParseFile((ParseFile));
            //localImageView.setImageResource(this.imageId.get(paramInt).intValue());

        }


        return localView;
    }
}
