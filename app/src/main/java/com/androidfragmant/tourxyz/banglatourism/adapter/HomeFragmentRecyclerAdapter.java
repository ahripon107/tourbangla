package com.androidfragmant.tourxyz.banglatourism.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.activities.ForumPostListActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.DivisionListActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.TourBlogActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.TourOperatorOffersListActivity;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentElement;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentImage;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.androidfragmant.tourxyz.banglatourism.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by amin on 6/27/16.
 */
public class HomeFragmentRecyclerAdapter extends RecyclerView.Adapter<HomeFragmentRecyclerAdapter.HomeFragmentViewHolder> {

    ArrayList<HomeFragmentElement> elements;
    Context context;
    ArrayList<HomeFragmentImage> homeFragmentImages1,homeFragmentImages2;
    SlideShowViewPagerAdapter slideShowViewPagerAdapter1,slideShowViewPagerAdapter2;
    HomeFragmentViewHolder holder1,holder2;
    Typeface tf;
    public HomeFragmentRecyclerAdapter(final Context context, ArrayList<HomeFragmentElement> elements) {
        this.elements = elements;
        this.context = context;
        this.tf = Typeface.createFromAsset(context.getAssets(),"font/MaterialDesignIcons.ttf");
        this.homeFragmentImages1 = new ArrayList<>();
        this.homeFragmentImages2 = new ArrayList<>();
        this.slideShowViewPagerAdapter1 = new SlideShowViewPagerAdapter(context,homeFragmentImages1);
        this.slideShowViewPagerAdapter2 = new SlideShowViewPagerAdapter(context,homeFragmentImages2);

        String url = Constants.FRONT_PAGE_IMAGE_LIST_URL;
        Log.d(Constants.TAG, url);

        if (isNetworkAvailable()) {
            FetchFromWeb.get(url,null,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String category = jsonObject.getString("category");
                            String imagename = jsonObject.getString("imagename");
                            String text = jsonObject.getString("text");
                            HomeFragmentImage homeFragmentImage = new HomeFragmentImage(id,category,imagename,text);
                            if (category.equals("browseplace")) {
                                homeFragmentImages1.add(homeFragmentImage);
                            } else if (category.equals("touroffer")) {
                                homeFragmentImages2.add(homeFragmentImage);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    slideShowViewPagerAdapter1.notifyDataSetChanged();
                    slideShowViewPagerAdapter2.notifyDataSetChanged();
                    addBottomDots1(0);
                    addBottomDots2(0);
                    Log.d(Constants.TAG, response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }
            });
        }


    }

    @Override
    public HomeFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlehomefragmentelement,parent,false);
        return new HomeFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeFragmentViewHolder holder, final int position) {
        HomeFragmentElement element = elements.get(position);
        holder.title.setTypeface(tf);
        holder.title.setText(element.getTitle());
        holder.button.setTypeface(tf);
        holder.button.setText(element.getButtonText());
        if (!isNetworkAvailable())
            holder.viewPagerImageSlideShow.setVisibility(View.GONE);
        if (position == 0) {
            this.holder1 = holder;
            slideShowViewPagerAdapter1.setImageCategoty("browseplace");
            holder.viewPagerImageSlideShow.setAdapter(slideShowViewPagerAdapter1);
        } else if (position == 1) {
            this.holder2  = holder;
            slideShowViewPagerAdapter2.setImageCategoty("touroffer");
            holder.viewPagerImageSlideShow.setAdapter(slideShowViewPagerAdapter2);
        } else if (position == 2) {
            holder.viewPagerImageSlideShow.setVisibility(View.GONE);
        } else if (position == 3) {
            holder.viewPagerImageSlideShow.setVisibility(View.GONE);
        }

        holder.viewPagerImageSlideShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position1) {
                if (position==0) {
                    addBottomDots1(position1);
                } else if (position == 1){
                    addBottomDots2(position1);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    boolean exists = false;
                    String[] files = context.fileList();
                    for (String file : files) {
                        if (file.equals("data.txt")) {
                            exists = true;
                            break;
                        } else {
                            exists = false;
                        }
                    }
                    if (!exists && !isNetworkAvailable()) {
                        Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_LONG).show();
                    }
                    else if (!exists && isNetworkAvailable()) {
                        String url = Constants.FETCH_PLACES_URL;
                        Log.d(Constants.TAG, url);
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Please wait...this may take a while...");
                        progressDialog.setTitle("Loading data");
                        progressDialog.show();

                        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                progressDialog.dismiss();
                                FileProcessor fileProcessor = new FileProcessor(context);
                                fileProcessor.writeToFile(response.toString());
                                Intent i = new Intent(context, DivisionListActivity.class);
                                context.startActivity(i);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                progressDialog.dismiss();
                                Toast.makeText(context, statusCode + "Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        FileProcessor fileProcessor = new FileProcessor(context);
                        fileProcessor.readFileAndProcess();
                        Intent i = new Intent(context, DivisionListActivity.class);
                        context.startActivity(i);
                    }
                }    else if (position == 1) {
                        Intent intent = new Intent(context, TourOperatorOffersListActivity.class);
                        context.startActivity(intent);

                } else if (position == 2) {
                    Intent i = new Intent(context, TourBlogActivity.class);
                    context.startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent(context, ForumPostListActivity.class);
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    private void addBottomDots1(int currentPage) {
        holder1.dots1 = new TextView[homeFragmentImages1.size()];

        int colorsActive = context.getResources().getColor(R.color.DarkGreen);
        int colorsInactive = context.getResources().getColor(R.color.MediumSpringGreen);

        holder1.dotsLayout.removeAllViews();
        for (int i = 0; i < holder1.dots1.length; i++) {
            holder1.dots1[i] = new TextView(context);
            holder1.dots1[i].setText(Html.fromHtml("&#8226;"));
            holder1.dots1[i].setTextSize(35);
            holder1.dots1[i].setTextColor(colorsInactive);
            holder1.dotsLayout.addView(holder1.dots1[i]);
        }
        if (holder1.dots1.length > 0)
            holder1.dots1[currentPage].setTextColor(colorsActive);
    }

    private void addBottomDots2(int currentPage) {
        holder2.dots2 = new TextView[homeFragmentImages2.size()];

        int colorsActive = context.getResources().getColor(R.color.DarkGreen);
        int colorsInactive = context.getResources().getColor(R.color.MediumSpringGreen);

        holder2.dotsLayout.removeAllViews();
        for (int i = 0; i < holder2.dots2.length; i++) {
            holder2.dots2[i] = new TextView(context);
            holder2.dots2[i].setText(Html.fromHtml("&#8226;"));
            holder2.dots2[i].setTextSize(35);
            holder2.dots2[i].setTextColor(colorsInactive);
            holder2.dotsLayout.addView(holder2.dots2[i]);
        }
        if (holder2.dots2.length > 0)
            holder2.dots2[currentPage].setTextColor(colorsActive);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    static class HomeFragmentViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected Button button;
        protected ViewPager viewPagerImageSlideShow;
        protected LinearLayout linearLayout;
        protected LinearLayout dotsLayout;
        private TextView[] dots1,dots2;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            title = ViewHolder.get(itemView, R.id.tourTitle);
            button = ViewHolder.get(itemView, R.id.button2);

            linearLayout = ViewHolder.get(itemView,R.id.cardcontainer);
            viewPagerImageSlideShow = ViewHolder.get(itemView,R.id.viewPagerImageSlideShow);
            dotsLayout = ViewHolder.get(itemView,R.id.layoutDots);
        }
    }
}
