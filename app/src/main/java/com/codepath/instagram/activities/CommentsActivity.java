package com.codepath.instagram.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.CommentsAdapter;
import com.codepath.instagram.fragments.NetworkDialogFragment;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by prajakta on 10/29/15.
 */
public class CommentsActivity extends AppCompatActivity {

    private static String TAG = "COMMENTS_ACTIVITY";
    String mMediaId;
    List<InstagramComment> comments;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        mMediaId = getIntent().getExtras().getString("mediaId");
        comments = getComments();
        context = this;


    }

    private List<InstagramComment> getComments() {
        InstagramClient.getCommentsFeed(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               // super.onSuccess(statusCode, headers, response);
                comments = Utils.decodeCommentsFromJsonResponse(response);
                Log.i(TAG, comments.toString());
                RecyclerView rvView = (RecyclerView)findViewById(R.id.rvComments);
                CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
                rvView.setAdapter(commentsAdapter);
                rvView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               // super.onFailure(statusCode, headers, responseString, throwable);
                NetworkDialogFragment networkDialogFragment = new NetworkDialogFragment();
                FragmentManager fm = getFragmentManager();
                networkDialogFragment.show(fm, "Network Dialog");
                //Log.i(TAG, comments.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                NetworkDialogFragment networkDialogFragment = new NetworkDialogFragment();
                FragmentManager fm = getFragmentManager();
                networkDialogFragment.show(fm, "Network Dialog");
            }

        }, mMediaId);
        return comments;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
