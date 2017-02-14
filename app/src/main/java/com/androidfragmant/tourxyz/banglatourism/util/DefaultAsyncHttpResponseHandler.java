package com.androidfragmant.tourxyz.banglatourism.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.androidfragmant.tourxyz.banglatourism.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @author Ripon
 */

public class DefaultAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
    private Handler handler;

    public DefaultAsyncHttpResponseHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String content = responseBody == null ? "" : new String(responseBody);

        if (BuildConfig.DEBUG)
            Log.d(Constants.TAG, content);

        sendMessageToTarget(Constants.SUCCESS, content);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        String content = responseBody == null ? "" : new String(responseBody);
        sendMessageToTarget(Constants.FAILURE, content);
    }

    private void sendMessageToTarget(int what, String obj) {
        Message.obtain(handler, what, obj).sendToTarget();
    }
}
