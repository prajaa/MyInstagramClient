package com.codepath.instagram.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.PostsAdapter;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

//import cz.msebera.android.httpclient.Header;

/**
 * Created by prajakta on 10/30/15.
 */
public class PostsFragment extends Fragment {

    List<InstagramPost> posts;
    Context mContext;
    View fView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fView = view;
        getPosts();


       // super.onViewCreated(view, savedInstanceState);
    }

    public List<InstagramPost> getPosts() {
        /*
       Day 1
       File filesDir = getFilesDir();
        try {
            JSONObject postsData = Utils.loadJsonFromAsset(HomeActivity.this, "popular.json");
            posts = Utils.decodePostsFromJsonResponse(postsData);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */

        //Day 2 - Fetch the Posts from Instagram API
        //Day 3 - change the Instagram client to be a Rest client
        InstagramClient instagramClient = new InstagramClient(mContext);
        instagramClient.getSelfFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                posts = Utils.decodePostsFromJsonResponse(response);
                RecyclerView rvPosts = (RecyclerView) fView.findViewById(R.id.rvPosts);
                SimpleVerticalSpacerItemDecoration rvSeparation = new SimpleVerticalSpacerItemDecoration(24);
                rvPosts.addItemDecoration(rvSeparation);
                PostsAdapter postsAdapter = new PostsAdapter(posts);
                rvPosts.setAdapter(postsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(mContext));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
              //  NetworkDialogFragment networkDialogFragment = new NetworkDialogFragment();
              //  FragmentManager fm = getFragmentManager();
              //  networkDialogFragment.show(fm,"");
               // Log.i(TAG, "Reaching network error");
                Toast.makeText(mContext, "FAIL", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (!isNetworkAvailable()) {
                 //   NetworkDialogFragment networkDialogFragment = new NetworkDialogFragment();
                 //   FragmentManager fm = getFragmentManager();
                 //   networkDialogFragment.show(fm, "Network Dialog");
                    Toast.makeText(mContext, "Network Failures", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, errorResponse.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        return posts;
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }





}
