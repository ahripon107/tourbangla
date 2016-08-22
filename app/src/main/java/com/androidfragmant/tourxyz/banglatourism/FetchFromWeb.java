package com.androidfragmant.tourxyz.banglatourism;

import android.os.Handler;
import android.os.Message;

import com.androidfragmant.tourxyz.banglatourism.util.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ripon on 6/3/16.
 */
public class FetchFromWeb {

    private AsyncHttpClient client;
    private Handler handler;

    public FetchFromWeb(Handler handler) {
        client = new AsyncHttpClient();
        this.handler = handler;
    }

    public void retreiveData(String url,RequestParams requestParams) {
        client.get(url,requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message.obtain(handler, Constants.SUCCESS,response).sendToTarget();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Message.obtain(handler, Constants.FAILURE,responseString).sendToTarget();
            }
        });
    }

    public void postData(String url,RequestParams requestParams) {
        client.post(url,requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Message.obtain(handler, Constants.SUCCESS,response).sendToTarget();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Message.obtain(handler, Constants.FAILURE,responseString).sendToTarget();
            }
        });
    }
}
