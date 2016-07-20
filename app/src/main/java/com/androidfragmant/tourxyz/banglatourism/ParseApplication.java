package com.androidfragmant.tourxyz.banglatourism;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Ripon on 7/20/15.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Batch.Push.setGCMSenderId("78317493023");

        // TODO : switch to live Batch Api Key before shipping
        //Batch.setConfig(new Config("DEV57888C87CB43EF32913115ACD3C")); // devloppement
        Batch.setConfig(new Config("57888C87C9514AA9FFB7706926386C")); // live

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
