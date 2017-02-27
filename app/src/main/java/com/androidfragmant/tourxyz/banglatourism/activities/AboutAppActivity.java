package com.androidfragmant.tourxyz.banglatourism.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidfragmant.tourxyz.banglatourism.R;
import com.androidfragmant.tourxyz.banglatourism.RoboAppCompatActivity;
import com.androidfragmant.tourxyz.banglatourism.util.Constants;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
@ContentView(R.layout.aboutapp)
public class AboutAppActivity extends RoboAppCompatActivity {

    @InjectView(R.id.tvabout)
    private TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface typeface = Constants.solaimanLipiFont(this);
        about.setTypeface(typeface);

        String string = "আমাদের এই দেশের আনাচে কানাচে অনেক সুন্দর জায়গা ছড়িয়ে ছিটিয়ে আছে। তার সবগুলোর খোজ খবর হয়ত আমরা জানি না। আবার অনেক পরিচিত জায়গা সম্পর্কেও খুব ভালো জানা না থাকায় সেখানে ঘুরতে যাওয়া হয়ে উঠে না। তাই স্বল্প পরিচিত বা অপরিচিত সুন্দর জায়গা গুলো তুলে ধরার পাশাপাশি জনপ্রিয় জায়গাগুলোকে আরও ভালোভাবে represent করার জন্য এই অ্যাপ Tour Bangla ।\n" +
                "\nএই অ্যাপ এ বিভাগ ও জেলা অনুযায়ী টুরিস্ট স্পট ব্রাউজ করা যাবে। এছাড়াও প্রত্যেক জায়গার উপর করা যাবে কমেন্ট এবং রেটিং। কোন জায়গা পছন্দ হলে তাকে wishlist এ অ্যাড করে রাখা যাবে।\n" +
                "\nনিজের ভ্রমণকাহিনী নিয়ে লেখা যাবে ব্লগ। এছাড়াও ব্লগে কমেন্ট অপশনও রাখা হয়েছে।\n" +
                "\n" +
                "কোনও জায়গা নিয়ে কিছু জানার থাকলে ফোরামে পোস্ট করতে পারেন। এছাড়া কোনও ট্যুরের হিসাব রাখার জন্য আছে Tour Cost Calculator । বিভিন্ন জেলার বাস, ট্রেন ও লঞ্চ এর ভাড়া ও সময়ও পাওয়া যাবে এই অ্যাপে। \n" +
                "\n" +
                "বিভিন্ন ট্যুর অপারেটর ও হোটেল এর অফারগুলো তুলে ধরার চেষ্টা করেছি আমরা। আপনি চাইলে এখান থেকে আপনার প্রয়োজনমত ট্যুর অপারেটর বা হোটেল বেছে নিতে পারবেন।\n" +
                "\nএমন কোনও জায়গার কথা যদি আপনার জানা থাকে যেটি এই অ্যাপ এ দেওয়া হয়নি তাহলে Suggest Place অপশন থেকে আপনি সেটি আমাদের সাজেস্ট করতে পারেন। অভিযোগ বা পরামর্শ জানানোর জন্য আছে ফিডব্যাক অপশন। অনুরোধ থাকবে অ্যাপ এর কোন বিষয় মনপুত না হলে লো রেটিং না দিয়ে দয়া করে ফিডব্যাকের মাধ্যমে আমাদের জানাবেন। আমরা চেষ্টা করব পরবর্তী আপডেটে সেটি সংশোধন করতে।\n\nHappy Touring :)\n";
        about.setText(string);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
