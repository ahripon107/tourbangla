package com.androidfragmant.tourxyz.banglatourism.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;

/**
 * Created by Ripon on 9/27/15.
 */
public class AboutAppFragment extends Fragment {

    public AboutAppFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.aboutapp, container, false);
        TextView about = (TextView) rootView.findViewById(R.id.tvabout);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "font/solaimanlipi.ttf");
        about.setTypeface(tf);
        String string = "আমাদের এই দেশের আনাচে কানাচে অনেক সুন্দর জায়গা ছড়িয়ে ছিটিয়ে আছে। তার সবগুলোর খোজ খবর হয়ত আমরা জানি না। আবার অনেক পরিচিত জায়গা সম্পর্কেও খুব ভালো জানা না থাকায় সেখানে ঘুরতে যাওয়া হয়ে উঠে না। তাই স্বল্প পরিচিত বা অপরিচিত সুন্দর জায়গা গুলো তুলে ধরার পাশাপাশি জনপ্রিয় জায়গাগুলোকে আরও ভালোভাবে represent করার জন্য এই অ্যাপ Tour Bangla ।\n" +
                "\n" +
                "এই অ্যাপ এ বিভাগ অনুযায়ী এবং জায়গার ধরণ অনুযায়ী টুরিস্ট স্পট ব্রাউজ করা যাবে। এছাড়াও প্রত্যেক জায়গার উপর করা যাবে কমেন্ট এবং রেটিং। কোন জায়গা পছন্দ হলে তাকে wishlist এ অ্যাড করে রাখা যাবে। \n" +
                "\n" +
                "নিজের ভ্রমণকাহিনী নিয়ে লেখা যাবে ব্লগ। এছাড়াও ব্লগে কমেন্ট অপশনও রাখা হয়েছে।\n" +
                "\n" +
                "এমন কোনও জায়গার কথা যদি আপনার জানা থাকে যেটি এই অ্যাপ এ দেওয়া হয়নি তাহলে Suggest Place অপশন থেকে আপনি সেটি আমাদের সাজেস্ট করতে পারেন। অভিযোগ বা পরামর্শ জানানোর জন্য আছে ফিডব্যাক অপশন। অনুরোধ থাকবে অ্যাপ এর কোন বিষয় মনপুত না হলে লো রেটিং না দিয়ে দয়া করে ফিডব্যাকের মাধ্যমে আমাদের জানাবেন। আমরা চেষ্টা করব পরবর্তী আপডেটে সেটি সংশোধন করতে। \n" +
                "\n" +
                "Happy Touring :)";
        about.setText(string);
        return rootView;
    }
}
