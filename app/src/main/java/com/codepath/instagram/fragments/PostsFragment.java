package com.codepath.instagram.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.PostsService;
import com.codepath.instagram.R;
import com.codepath.instagram.adapter.PostsAdapter;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
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
    SwipeRefreshLayout swipeContainer;
    PostsAdapter postsAdapter;
    InstagramClientDatabase instagramClientDatabase;// = InstagramClientDatabase.getInstance(mContext);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        instagramClientDatabase = InstagramClientDatabase.getInstance(context);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fView = view;
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        postsAdapter = new PostsAdapter();

        RecyclerView rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        SimpleVerticalSpacerItemDecoration rvSeparation = new SimpleVerticalSpacerItemDecoration(24);
        rvPosts.addItemDecoration(rvSeparation);
        rvPosts.setAdapter(postsAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPosts.setLayoutManager(new LinearLayoutManager(mContext));

        swipeContainer.setRefreshing(false);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostsService();
            }
        });
        getPostsService();

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(PostsService.ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(postsReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(postsReceiver);
    }

    public void getPostsService() {
        Intent i = new Intent(mContext, PostsService.class);
        i.putExtra(PostsService.INTENT_URL, InstagramClient.REST_URL+InstagramClient.SELF_FEED_URL);
        mContext.startService(i);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private BroadcastReceiver postsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(PostsService.INTENT_STATUS_CODE, 0) == 200) {
                String response = intent.getStringExtra(PostsService.INTENT_DATA);
                postsAdapter.clear();
                try {
                    JSONObject resp = new JSONObject(response);
                    posts = Utils.decodePostsFromJsonResponse(resp);
                    postsAdapter.addAll(posts);
                    swipeContainer.setRefreshing(false);
                    instagramClientDatabase.emptyAllTables();
                    instagramClientDatabase.addInstagramPosts(posts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                if (!isNetworkAvailable()) {
                    posts = instagramClientDatabase.getAllInstagramPosts();
                    postsAdapter.addAll(posts);
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(mContext, "NO Network!! RETRIEVING THE LAST SUCCESS DATA", Toast.LENGTH_LONG).show();
                } else {
                    posts = instagramClientDatabase.getAllInstagramPosts();
                    postsAdapter.addAll(posts);
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(mContext, "RETRIEVING THE LAST SUCCESS DATA", Toast.LENGTH_LONG).show();
                }
            }

        }
    };




}
