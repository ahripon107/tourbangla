package com.androidfragmant.tourxyz.banglatourism.util;


import android.content.Context;
import android.graphics.Typeface;

/**
 * @author Ripon
 */
public class Constants {
    public static final String TAG = "tourxyz";
    public static final String KEY = "key";
    public static final String KEY_VALUE = "bl905577";

    public static final String DIVISION_NAME = "divisionName";
    public static final String DISTRICT_NAME = "districtName";

    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;

    public static final String TOUR_OPERATOR_OFFER_URL = "http://apisea.xyz/TourBangla/apis/v1/tourOperatorOffer.php";
    public static final String FRONT_PAGE_IMAGE_LIST_URL = "http://apisea.xyz/TourBangla/apis/v1/FetchHomeFragmentImageList.php";

    /************************ PLACE RELATED URL ***************************/
    public static final String FETCH_PLACES_URL = "http://apisea.xyz/TourBangla/apis/v1/FetchPlaces.php";
    public static final String INSERT_PLACE_COMMENT_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertPlaceComment.php";
    public static final String FETCH_PLACE_COMMENTS_URL = "http://apisea.xyz/TourBangla/apis/v1/PlaceComments.php";

    /************************ FORUM RELATED URL ***************************/
    public static final String FETCH_FORUM_POSTS_URL = "http://apisea.xyz/TourBangla/apis/v1/FetchForumPost.php";
    public static final String INSERT_FORUM_POST_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertForumPost.php";
    public static final String INSERT_FORUM_POST_COMMENT_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertForumPostComment.php";
    public static final String FETCH_FORUM_POST_COMMENTS = "http://apisea.xyz/TourBangla/apis/v1/FetchForumPostComments.php";

    /************************ BLOG RELATED URL ****************************/
    public static final String FETCH_BLOG_POSTS_URL = "http://apisea.xyz/TourBangla/apis/v3/FetchTourBlogs.php";
    public static final String FETCH_BLOG_DETAILS_URL = "http://apisea.xyz/TourBangla/apis/v3/FetchBlogDetails.php";
    public static final String INSERT_BLOG_POST_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertBlogPost.php";
    public static final String INSERT_BLOG_POST_COMMENT_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertBlogPostComment.php";
    public static final String FETCH_BLOG_POST_COMMENTS_URL = "http://apisea.xyz/TourBangla/apis/v1/BlogPostComments.php";

    /************************* FEEDBACK URL *****************************/
    public static final String SEND_FEEDBACK_URL = "http://apisea.xyz/TourBangla/apis/v1/feedback.php";

    /************************* SUGGEST NEW PLACE URL ************************/
    public static final String SUGGEST_NEW_PLACE_URL = "http://apisea.xyz/TourBangla/apis/v2/SuggestNewPlace.php";

    /************************* FARE URL *********************************/
    public static final String FETCH_FARES_URL = "http://apisea.xyz/TourBangla/apis/v1/FetchFares.php";

    public static final String FETCH_YOUTUBE_VIDEOS_URL = "http://apisea.xyz/TourBangla/apis/v1/FetchYoutubeVideos.php";

    public static final String INSERT_VISITED_PLACE_ELEMENT_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertVisitedPlaceElement.php";

    public static final String INSERT_VISITED_BLOG_POST_URL = "http://apisea.xyz/TourBangla/apis/v1/InsertVisitedBlogPost.php";

    public static final String WISHLIST_PREFERENCE_FILE = "wishlist";

    public static final String SOLAIMAN_LIPI_FONT = "font/solaimanlipi.ttf";

    public static final String ONE_PLUS_TEST_DEVICE = "7D3F3DF2A7214E839DBE70BE2132D5B9";

    public static final String TOUR_COST_PLACE_PREFERENCE_FILE = "cost";

    public static final String COST_PLACE_ID_PREFERENCE_FILE = "idpref";

    public static final String COST_ITEM_PREFERENCE_FILE = "cost_item";

    public static final String COST_ITEM_ID_PREFERENCE_FILE = "cost_item_id";

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 119 * MINUTE_MILLIS) {
            return "1 hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static Typeface solaimanLipiFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), Constants.SOLAIMAN_LIPI_FONT);
    }
}
