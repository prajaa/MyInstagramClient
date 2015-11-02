package com.codepath.instagram.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.SearchUserResultAdapter;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by prajakta on 10/30/15.
 */
public class SearchResultUserFragment extends Fragment {
    List<InstagramUser> users;
    Context mContext;
    View mView;
    ProgressBar progress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //progress = (ProgressBar) view.findViewById(R.id.progress);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar.
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InstagramClient instagramClient = new InstagramClient(mContext);
              //  progress.setVisibility(ProgressBar.VISIBLE);
                instagramClient.getUserSearchFeed(new JsonHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                       // progress.setVisibility(ProgressBar.GONE);
                        if (isNetworkAvailable()) {
                            Toast.makeText(mContext, R.string.network_error_message, Toast.LENGTH_SHORT);
                        } else
                            Toast.makeText(mContext, errorResponse.toString(), Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                     //   progress.setVisibility(ProgressBar.GONE);
                        users = Utils.decodeUsersFromJsonResponse(response);
                        RecyclerView rvSearchResults = (RecyclerView) mView.findViewById(R.id.rvPosts);
                        SearchUserResultAdapter srchAdapter = new SearchUserResultAdapter(users);
                        rvSearchResults.setAdapter(srchAdapter);
                        rvSearchResults.setLayoutManager(new LinearLayoutManager(mContext));

                    }
                }, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        Log.i("","Set the on query change");

    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
