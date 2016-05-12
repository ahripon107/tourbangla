package com.sfuronlabs.ripon.tourbangla;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Ripon on 7/20/15.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Parse.enableLocalDatastore(this);

        Parse.initialize(this,"jDhqEOdPSy2xM1D5mjappZ8aOkqldhM0d9Q9nJjg","ZDkhwiNZqT0qzZu7Q82d5YMVIEepMKLPNiDLiPVN");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
