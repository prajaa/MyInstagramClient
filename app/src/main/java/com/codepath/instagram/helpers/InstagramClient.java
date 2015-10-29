package com.codepath.instagram.helpers;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by prajakta on 10/28/15.
 */
public class InstagramClient {


    private static String mMediaId;
    private static final String POPULAR_FEED_URL = "popular?client_id=";
    private static final String COMMENTS_FEED_URL = "/comments?client_id=";
    private static final String BASE_FEED_URL = "https://api.instagram.com/v1/media/";


    private static final String CLIENT_ID = "4280c802ff114fe9a8fbee03fd0c0dc7";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(BASE_FEED_URL+POPULAR_FEED_URL+CLIENT_ID, null, responseHandler);

    }

    public static void getCommentsFeed(JsonHttpResponseHandler responseHandler, String mediaId) {
        mMediaId = mediaId;
        String api = BASE_FEED_URL+mMediaId+COMMENTS_FEED_URL+CLIENT_ID;
        Log.i("COMMENTS_ACTIVITY", "Getting the comments Feed "+api);
        client.get(api, null, responseHandler);
    }





}
