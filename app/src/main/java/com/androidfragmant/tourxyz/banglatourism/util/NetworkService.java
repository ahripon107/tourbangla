package com.androidfragmant.tourxyz.banglatourism.util;

import android.os.Handler;
import android.util.Log;

import com.androidfragmant.tourxyz.banglatourism.model.BlogPost;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @author Ripon
 */
@Singleton
public class NetworkService {

    @Inject
    private AsyncHttpClient httpClient;

    public void fetchComments(String url, int id, Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(Constants.KEY, Constants.KEY_VALUE);
        requestParams.put("id", String.valueOf(id));

        httpClient.get(url, requestParams, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void insertComment(String comment, String name, String url, int id, String timestamp, Handler handler) {
        RequestParams params = new RequestParams();
        params.put(Constants.KEY, Constants.KEY_VALUE);
        params.put("id", String.valueOf(id));
        params.put("name", name);
        params.put("comment", comment);
        params.put("timestamp", timestamp);

        httpClient.post(url, params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void sendFeedback(String title,String details,Handler handler) {
        RequestParams params = new RequestParams();
        params.put(Constants.KEY, Constants.KEY_VALUE);
        params.put("title", title);
        params.put("details", details);

        httpClient.post(Constants.SEND_FEEDBACK_URL,params,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchFrontPageImageList(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY, Constants.KEY_VALUE);

        httpClient.get(Constants.FRONT_PAGE_IMAGE_LIST_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchPlaces(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY, Constants.KEY_VALUE);

        httpClient.get(Constants.FETCH_PLACES_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchTopPlaces(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(Constants.KEY, Constants.KEY_VALUE);

        httpClient.get(Constants.FETCH_TOP_PLACES,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchSpecificPlace(int id, Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(Constants.KEY, Constants.KEY_VALUE);
        requestParams.put("id",id);

        httpClient.get(Constants.FETCH_SPECIFIC_PLACE,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void suggestNewPlace(String hotels, String howtogo, String description, String division,
                                String address, String name,
                                String email, Handler handler) {
        RequestParams params = new RequestParams();
        params.put(Constants.KEY, Constants.KEY_VALUE);
        params.put("activity_hotels", hotels);
        params.put("howtogo", howtogo);
        params.put("description", description);
        params.put("division", division);
        params.put("address", address);
        params.put("name", name);
        params.put("email",email);

        httpClient.post(Constants.SUGGEST_NEW_PLACE_URL,params,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchYoutubeVideos(int id, Handler handler) {
        RequestParams params = new RequestParams();
        params.add(Constants.KEY, Constants.KEY_VALUE);
        params.add("id", String.valueOf(id));

        httpClient.get(Constants.FETCH_YOUTUBE_VIDEOS_URL,params,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchTourOperatorOffers(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);

        httpClient.get(Constants.TOUR_OPERATOR_OFFER_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void insertVisitedBlogPost(BlogPost blogPost, String timestamp, Handler handler) {
        RequestParams paramsBlog = new RequestParams();
        paramsBlog.put(Constants.KEY, Constants.KEY_VALUE);
        paramsBlog.put("blogid",blogPost.getId());
        paramsBlog.put("blogtitle",blogPost.getTitle());
        paramsBlog.put("timestamp",timestamp);

        httpClient.post(Constants.INSERT_VISITED_BLOG_POST_URL,paramsBlog,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchBlogPostList(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);
        Log.d(Constants.TAG, Constants.FETCH_BLOG_POSTS_URL);

        httpClient.get(Constants.FETCH_BLOG_POSTS_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchBlogDetails(String id, Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);
        requestParams.put("id",id);

        httpClient.get(Constants.FETCH_BLOG_DETAILS_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void insertNewTourBlog(String image, String title, String details,
                                  String tags, String name, String timestamp, Handler handler) {
        RequestParams params = new RequestParams();
        params.put(Constants.KEY,Constants.KEY_VALUE);
        params.put("image", image);
        params.put("title",title);
        params.put("details",details);
        params.put("tags",tags);
        params.put("name",name);
        params.put("timestamp",timestamp);

        httpClient.post(Constants.INSERT_BLOG_POST_URL,params,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void insertVisitedPlace(String placeName, String timestamp,Handler handler) {
        RequestParams paramsName = new RequestParams();
        paramsName.put(Constants.KEY, Constants.KEY_VALUE);
        paramsName.put("placename",placeName);
        paramsName.put("timestamp",timestamp);

        httpClient.post(Constants.INSERT_VISITED_PLACE_ELEMENT_URL,paramsName,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchForumPostComments(int id, Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY, Constants.KEY_VALUE);
        requestParams.add("postid", String.valueOf(id));

        httpClient.get(Constants.FETCH_FORUM_POST_COMMENTS,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void insertForumPostComment(String name, String postid, String comment, String timestamp, Handler handler) {
        RequestParams params = new RequestParams();
        params.put(Constants.KEY, Constants.KEY_VALUE);
        params.put("name", name);
        params.put("postid", postid);
        params.put("comment", comment);
        params.put("timestamp", timestamp);

        httpClient.post(Constants.INSERT_FORUM_POST_COMMENT_URL, params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchForumPostList(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY,Constants.KEY_VALUE);
        Log.d(Constants.TAG, Constants.FETCH_FORUM_POSTS_URL);

        httpClient.get(Constants.FETCH_FORUM_POSTS_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void insertForumPost(String name, String question, String timestamp, Handler handler) {
        RequestParams params = new RequestParams();
        params.put(Constants.KEY, Constants.KEY_VALUE);
        params.put("name", name);
        params.put("question", question);
        params.put("timestamp", timestamp);

        httpClient.post(Constants.INSERT_FORUM_POST_URL, params, new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchFares(Handler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY, Constants.KEY_VALUE);

        httpClient.get(Constants.FETCH_FARES_URL,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }

    public void fetchTourOfferDetails(int id, Handler handler){
        RequestParams requestParams = new RequestParams();
        requestParams.add(Constants.KEY, Constants.KEY_VALUE);
        requestParams.add("id", String.valueOf(id));

        httpClient.get(Constants.TOUR_OPERATOR_OFFER_DETAILS,requestParams,new DefaultAsyncHttpResponseHandler(handler));
    }
}
