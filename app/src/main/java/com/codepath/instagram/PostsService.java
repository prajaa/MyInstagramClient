package com.codepath.instagram;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by prajakta on 11/3/15.
 */
public class PostsService extends IntentService {
    public static final String LOG_TAG = "PostsService";
    public static final String ACTION = "com.codepath.instagram.PostsService";
    public static final String INTENT_URL = "INTENT_URL";
    public static final String INTENT_STATUS_CODE = "INTENT_STATUS_CODE";
    public static final String INTENT_HEADERS = "INTENT_HEADERS";
    public static final String INTENT_DATA = "INTENT_DATA";
    public static final String INTENT_THROWABLE = "INTENT_THROWABLE";
    Context mContext;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PostsService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = this;
        if (intent != null & intent.hasExtra(INTENT_URL)) {
            InstagramClient client = ((MainApplication) this.getApplication()).getInstagramClient();
            client.getSelfFeed(intent.getStringExtra(INTENT_URL), new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                  //  super.onFailure(statusCode, headers, throwable, errorResponse);
                    Intent broadcast = new Intent(ACTION);
                    broadcast.putExtra(INTENT_STATUS_CODE, statusCode);
                    if (errorResponse != null) {
                        broadcast.putExtra(INTENT_DATA, errorResponse.toString());
                        broadcast.putExtra(INTENT_THROWABLE, errorResponse.toString());
                    }
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
                  //  sendBroadcast(broadcast);
                    Log.d(LOG_TAG, "onFailure");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                   // super.onSuccess(statusCode, headers, response);
                    Intent broadcast = new Intent(ACTION);
                    broadcast.putExtra(INTENT_STATUS_CODE, statusCode);
                    broadcast.putExtra(INTENT_DATA, response.toString());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
                    Log.d("PRAJ", "onSuccess Posts Data "+response.toString());
                }


            });

        }
    }
}
