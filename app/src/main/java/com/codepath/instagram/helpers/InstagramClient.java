package com.codepath.instagram.helpers;

import android.content.Context;
import android.util.Log;

import com.codepath.instagram.networking.InstagramApi;
import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.scribe.builder.api.Api;
import org.scribe.model.Token;

/**
 * Created by prajakta on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {


    private static String mMediaId;
    public static final String POPULAR_FEED_URL = "media/popular?client_id=";
    public static final String COMMENTS_FEED_URL = "/media/comments?client_id=";
    public static final String SELF_FEED_URL = "users/self/feed";
    public static final String USER_SEARCH_FEED_URL = "users/search?q=";
    public static final String TAG_SEARCH_FEED_URL = "tags/search?q=";
    public static final String SEARCH_FEED_URL = "search?q=";
    public static final String USERS = "users/";
    public static final String TAGS = "tags/";
    public static final String PHOTO_FEED_URL = "/media/recent/";
    public static final String FOLLOWING_FEEL_URL = "/follows";
    public static final String FOLLOWERS_FEED_URL = "/followed-by";

    public static final String REST_URL = "https://api.instagram.com/v1/";


    public static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    public static final String REST_CONSUMER_KEY = "4280c802ff114fe9a8fbee03fd0c0dc7";
    public static final String REST_CONSUMER_SECRET = "65fac2aefb674e9a8b7fff2df4043800";
    //public static final String REST_CONSUMER_KEY = "7f5321002cc04089b778e463cd87953f";
   //public static final String REST_CONSUMER_SECRET = "a9980e6933814fd3848dba9f6b370b63";
    public static final String REST_CALLBACK_URL = Constants.REDIRECT_URI;
    public static final String REDIRECT_URI = Constants.REDIRECT_URI;
    public static final String SCOPE = Constants.SCOPE;
    private AsyncHttpClient mSyncClient = new SyncHttpClient();



    private static final String CLIENT_ID = "4280c802ff114fe9a8fbee03fd0c0dc7";


    public InstagramClient(Context c) {
        super(c, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL, SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + POPULAR_FEED_URL + CLIENT_ID, null, responseHandler);

    }

    public void getSelfFeed(String url, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        mSyncClient.get(url, params, responseHandler);
       // client.get(REST_URL + SELF_FEED_URL, params, responseHandler);
        //client.get(REST_URL + SELF_FEED_URL, null, responseHandler);
    }

    public void getCommentsFeed(JsonHttpResponseHandler responseHandler, String mediaId) {
        mMediaId = mediaId;
        String api = REST_URL +mMediaId+COMMENTS_FEED_URL+CLIENT_ID;
        Log.i("COMMENTS_ACTIVITY", "Getting the comments Feed " + api);
        client.get(api, null, responseHandler);
    }

    public void getUserSearchFeed(JsonHttpResponseHandler responseHandler, String searchTerm) {
        String api = REST_URL + USERS + SEARCH_FEED_URL + searchTerm;
        client.get(api, null, responseHandler);
    }

    public void getTagSearchFeed(JsonHttpResponseHandler responseHandler, String searchTerm) {
        String api = REST_URL + TAGS + SEARCH_FEED_URL + searchTerm;
        client.get(api, null, responseHandler);
    }

    public void getUsersPhotoFeed(JsonHttpResponseHandler responseHandler, String userId) {
        String api = REST_URL + USERS + userId + PHOTO_FEED_URL;
       // Log.i("PROFILE", api);
        client.get(api, null, responseHandler);
    }

    public void getTagsPhotoFeed(JsonHttpResponseHandler responseHandler, String tagId) {
        String api = REST_URL + TAGS + tagId + PHOTO_FEED_URL;
       // Log.i("PROFILE", api);
        client.get(api, null, responseHandler);
    }

    public void getFollowersFeed(JsonHttpResponseHandler responseHandler, String userId) {
        String api = REST_URL + USERS + userId + FOLLOWERS_FEED_URL;
      //  Log.i("PROFILE", api);
        client.get(api, null, responseHandler);
    }

    public void getFollowingFeed(JsonHttpResponseHandler responseHandler, String userId) {
        String api = REST_URL + USERS + userId + FOLLOWING_FEEL_URL;
       // Log.i("PROFILE", api);
        client.get(api, null, responseHandler);
    }


}
