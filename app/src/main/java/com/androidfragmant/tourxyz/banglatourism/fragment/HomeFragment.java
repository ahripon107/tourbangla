package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
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
import com.androidfragmant.tourxyz.banglatourism.util.DefaultMessageHandler;
import com.androidfragmant.tourxyz.banglatourism.util.NetworkService;
import com.google.gson.Gson;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class HomeFragment extends RoboFragment {

    private ArrayList<HomeFragmentElement> elements;

    @InjectView(R.id.placeViewPagerImageSlideShow)
    private ViewPager placeImageViewPager;

    @InjectView(R.id.offerViewPagerImageSlideShow)
    private ViewPager offerImageViewPager;

    @InjectView(R.id.placeImageDots)
    private LinearLayout placeImageDotsLayout;

    @InjectView(R.id.offerImageDots)
    private LinearLayout offerImageDotsLayout;

    @InjectView(R.id.placeCardTitle)
    private TextView placeCardTitle;

    @InjectView(R.id.offerCardTitle)
    private TextView offerCardTitle;

    @InjectView(R.id.blogCardTitle)
    private TextView blogCardTitle;

    @InjectView(R.id.forumCardTitle)
    private TextView forumCardTitle;

    @InjectView(R.id.fareCardTitle)
    private TextView fareCardTitle;

    @InjectView(R.id.btnExplorePlace)
    private Button explorePlace;

    @InjectView(R.id.btnExploreOffer)
    private Button exploreOffer;

    @InjectView(R.id.btnExploreBlog)
    private Button exploreBlog;

    @InjectView(R.id.btnExploreForum)
    private Button exploreForum;

    @InjectView(R.id.btnExploreFare)
    private Button exploreFare;

    @Inject
    private ArrayList<HomeFragmentImage> placeImages;

    @Inject
    private ArrayList<HomeFragmentImage> offerImages;

    @Inject
    private NetworkService networkService;

    private Typeface tf;
    private SlideShowViewPagerAdapter placeViewPagerAdapter, offerViewPagerAdapter;
    private TextView[] dots1, dots2;


    public HomeFragment() {
        elements = new ArrayList<>();
        elements.add(new HomeFragmentElement("অনিন্দ্যসুন্দর বাংলাদেশের অসাধারণ টুরিস্ট স্পটগুলো এক্সপ্লোর করুন!", "Explore Places"));
        elements.add(new HomeFragmentElement("ট্যুর অপারেটর ও হোটেলগুলোর বিভিন্ন অফার উপভোগ করুন!", "Tour offers"));
        elements.add(new HomeFragmentElement("অন্যের ভ্রমণকাহিনী পড়ুন এবং নিজের ভ্রমণ অভিজ্ঞতা শেয়ার করুন ট্যুর ব্লগ এর মাধ্যমে!", "Tour Blog"));
        elements.add(new HomeFragmentElement("কোনও জায়গা সম্পর্কে কিছু জানতে চাইলে ফোরামে জিজ্ঞাসা করুন!", "Forum"));
        elements.add(new HomeFragmentElement("আন্তঃ জেলা ভাড়ার তালিকা দেখুন!", "Fare"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeViewPagerAdapter = new SlideShowViewPagerAdapter(getContext(), placeImages);
        offerViewPagerAdapter = new SlideShowViewPagerAdapter(getContext(), offerImages);
        tf = Constants.solaimanLipiFont(getContext());

        if (!isNetworkAvailable()) {
            placeImageViewPager.setVisibility(View.GONE);
            offerImageViewPager.setVisibility(View.GONE);
        }

        if (isNetworkAvailable()) {
            networkService.fetchFrontPageImageList(new DefaultMessageHandler(getContext()) {
                @Override
                public void onSuccess(Message msg) {
                    String string = (String) msg.obj;
                    try {
                        JSONObject response = new JSONObject(string);

                        JSONArray jsonArray = response.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Gson gson = new Gson();
                            HomeFragmentImage homeFragmentImage = gson.fromJson(String.valueOf(jsonObject), HomeFragmentImage.class);

                            if (homeFragmentImage.getCategory().equals("browseplace")) {
                                placeImages.add(homeFragmentImage);
                            } else if (homeFragmentImage.getCategory().equals("touroffer")) {
                                offerImages.add(homeFragmentImage);
                            }
                        }

                        placeViewPagerAdapter.notifyDataSetChanged();
                        offerViewPagerAdapter.notifyDataSetChanged();
                        addBottomDots1(0);
                        addBottomDots2(0);
                        Log.d(Constants.TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                    networkService.fetchPlaces(new DefaultMessageHandler(getContext(),true) {
                        @Override
                        public void onSuccess(Message msg) {
                            String response = (String) msg.obj;
                            FileProcessor fileProcessor = new FileProcessor(getContext());
                            fileProcessor.writeToFile(response);
                            Intent i = new Intent(getActivity(), DivisionListActivity.class);
                            getActivity().startActivity(i);
                            getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    });
                } else {
                    FileProcessor fileProcessor = new FileProcessor(getContext());
                    fileProcessor.readFileAndProcess();
                    Intent i = new Intent(getActivity(), DivisionListActivity.class);
                    getActivity().startActivity(i);
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
            }
        });

        exploreOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TourOperatorOffersListActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        exploreBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TourBlogActivity.class);
                getActivity().startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        exploreForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ForumPostListActivity.class);
                getActivity().startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        exploreFare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FareActivity.class);
                getActivity().startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
