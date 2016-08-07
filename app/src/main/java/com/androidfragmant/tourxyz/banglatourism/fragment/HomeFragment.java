package com.androidfragmant.tourxyz.banglatourism.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfragmant.tourxyz.banglatourism.FetchFromWeb;
import com.androidfragmant.tourxyz.banglatourism.FileProcessor;
import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.activities.DivisionListActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.FareActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.ForumPostListActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.TourBlogActivity;
import com.androidfragmant.tourxyz.banglatourism.activities.TourOperatorOffersListActivity;
import com.androidfragmant.tourxyz.banglatourism.adapter.SlideShowViewPagerAdapter;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentElement;
import com.androidfragmant.tourxyz.banglatourism.model.HomeFragmentImage;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Ripon on 6/11/15.
 */
public class HomeFragment extends RoboFragment {

    ArrayList<HomeFragmentElement> elements;

    @InjectView(R.id.placeViewPagerImageSlideShow)
    ViewPager placeImageViewPager;

    @InjectView(R.id.offerViewPagerImageSlideShow)
    ViewPager offerImageViewPager;

    @InjectView(R.id.placeImageDots)
    LinearLayout placeImageDotsLayout;

    @InjectView(R.id.offerImageDots)
    LinearLayout offerImageDotsLayout;

    @InjectView(R.id.placeCardTitle)
    TextView placeCardTitle;

    @InjectView(R.id.offerCardTitle)
    TextView offerCardTitle;

    @InjectView(R.id.blogCardTitle)
    TextView blogCardTitle;

    @InjectView(R.id.forumCardTitle)
    TextView forumCardTitle;

    @InjectView(R.id.fareCardTitle)
    TextView fareCardTitle;

    @InjectView(R.id.btnExplorePlace)
    Button explorePlace;

    @InjectView(R.id.btnExploreOffer)
    Button exploreOffer;

    @InjectView(R.id.btnExploreBlog)
    Button exploreBlog;

    @InjectView(R.id.btnExploreForum)
    Button exploreForum;

    @InjectView(R.id.btnExploreFare)
    Button exploreFare;

    Typeface tf;
    ArrayList<HomeFragmentImage> placeImages, offerImages;
    SlideShowViewPagerAdapter placeViewPagerAdapter, offerViewPagerAdapter;
    TextView[] dots1, dots2;


    public HomeFragment() {
        elements = new ArrayList<>();
        elements.add(new HomeFragmentElement("Explore amazing tourist spots of beautiful Bangladesh!","Explore Places"));
        elements.add(new HomeFragmentElement("Explore offers from tour operators and hotels!","Tour offers"));
        elements.add(new HomeFragmentElement("Want to share your and read other's tour experience? Explore Tour Blog!","Tour Blog"));
        elements.add(new HomeFragmentElement("Want to know anything about a place? Ask in Forum!","Forum"));
        elements.add(new HomeFragmentElement("Explore inter district fare!","Fare"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeImages = new ArrayList<>();
        offerImages = new ArrayList<>();
        placeViewPagerAdapter = new SlideShowViewPagerAdapter(getContext(),placeImages);
        offerViewPagerAdapter = new SlideShowViewPagerAdapter(getContext(),offerImages);
        tf = Typeface.createFromAsset(getContext().getAssets(),Constants.SOLAIMAN_LIPI_FONT);
        String url = Constants.FRONT_PAGE_IMAGE_LIST_URL;
        Log.d(Constants.TAG, url);

        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);

        if (!isNetworkAvailable()) {
            placeImageViewPager.setVisibility(View.GONE);
            offerImageViewPager.setVisibility(View.GONE);
        }


        if (isNetworkAvailable()) {
            FetchFromWeb.get(url, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String category = jsonObject.getString("category");
                            String imagename = jsonObject.getString("imagename");
                            String text = jsonObject.getString("text");
                            HomeFragmentImage homeFragmentImage = new HomeFragmentImage(id, category, imagename, text);
                            if (category.equals("browseplace")) {
                                placeImages.add(homeFragmentImage);
                            } else if (category.equals("touroffer")) {
                                offerImages.add(homeFragmentImage);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    placeViewPagerAdapter.notifyDataSetChanged();
                    offerViewPagerAdapter.notifyDataSetChanged();
                    addBottomDots1(0);
                    addBottomDots2(0);
                    Log.d(Constants.TAG, response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }
            });
        }

        placeViewPagerAdapter.setImageCategoty("browseplace");
        offerViewPagerAdapter.setImageCategoty("touroffer");

        placeImageViewPager.setAdapter(placeViewPagerAdapter);
        offerImageViewPager.setAdapter(offerViewPagerAdapter);

        placeImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position1) {
                addBottomDots1(position1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        offerImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position1) {
                addBottomDots2(position1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        explorePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exists = false;
                String[] files = getActivity().fileList();
                for (String file : files) {
                    if (file.equals("data.txt")) {
                        exists = true;
                        break;
                    } else {
                        exists = false;
                    }
                }
                if (!exists && !isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                } else if (!exists && isNetworkAvailable()) {
                    String url = Constants.FETCH_PLACES_URL;
                    Log.d(Constants.TAG, url);
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait...this may take a while...");
                    progressDialog.setTitle("Loading data");
                    progressDialog.show();

                    FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            progressDialog.dismiss();
                            FileProcessor fileProcessor = new FileProcessor(getContext());
                            fileProcessor.writeToFile(response.toString());
                            Intent i = new Intent(getActivity(), DivisionListActivity.class);
                            getActivity().startActivity(i);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), statusCode + "Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    FileProcessor fileProcessor = new FileProcessor(getContext());
                    fileProcessor.readFileAndProcess();
                    Intent i = new Intent(getActivity(), DivisionListActivity.class);
                    getActivity().startActivity(i);
                }
            }
        });

        exploreOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TourOperatorOffersListActivity.class);
                getActivity().startActivity(intent);
            }
        });

        exploreBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TourBlogActivity.class);
                getActivity().startActivity(i);
            }
        });

        exploreForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ForumPostListActivity.class);
                getActivity().startActivity(i);
            }
        });

        exploreFare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),FareActivity.class);
                getActivity().startActivity(i);
            }
        });

        explorePlace.setText(elements.get(0).getButtonText());
        exploreOffer.setText(elements.get(1).getButtonText());
        exploreBlog.setText(elements.get(2).getButtonText());
        exploreForum.setText(elements.get(3).getButtonText());
        exploreFare.setText(elements.get(4).getButtonText());

        placeCardTitle.setTypeface(tf);
        offerCardTitle.setTypeface(tf);
        blogCardTitle.setTypeface(tf);
        forumCardTitle.setTypeface(tf);
        fareCardTitle.setTypeface(tf);

        placeCardTitle.setText(elements.get(0).getTitle());
        offerCardTitle.setText(elements.get(1).getTitle());
        blogCardTitle.setText(elements.get(2).getTitle());
        forumCardTitle.setText(elements.get(3).getTitle());
        fareCardTitle.setText(elements.get(4).getTitle());
    }

    private void addBottomDots1(int currentPage) {
        dots1 = new TextView[placeImages.size()];

        int colorsActive = getContext().getResources().getColor(R.color.DarkGreen);
        int colorsInactive = getContext().getResources().getColor(R.color.MediumSpringGreen);

        placeImageDotsLayout.removeAllViews();
        for (int i = 0; i < dots1.length; i++) {
            dots1[i] = new TextView(getContext());
            dots1[i].setText(Html.fromHtml("&#8226;"));
            dots1[i].setTextSize(35);
            dots1[i].setTextColor(colorsInactive);
            placeImageDotsLayout.addView(dots1[i]);
        }
        if (dots1.length > 0)
            dots1[currentPage].setTextColor(colorsActive);
    }

    private void addBottomDots2(int currentPage) {
        dots2 = new TextView[offerImages.size()];

        int colorsActive = getContext().getResources().getColor(R.color.DarkGreen);
        int colorsInactive = getContext().getResources().getColor(R.color.MediumSpringGreen);

        offerImageDotsLayout.removeAllViews();
        for (int i = 0; i < dots2.length; i++) {
            dots2[i] = new TextView(getContext());
            dots2[i].setText(Html.fromHtml("&#8226;"));
            dots2[i].setTextSize(35);
            dots2[i].setTextColor(colorsInactive);
            offerImageDotsLayout.addView(dots2[i]);
        }
        if (dots2.length > 0)
            dots2[currentPage].setTextColor(colorsActive);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
