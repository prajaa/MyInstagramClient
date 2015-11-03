package com.codepath.instagram.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.SearchTagResultsAdapter;
import com.codepath.instagram.adapter.SearchUserResultAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by prajakta on 11/1/15.
 */
public class SearchResultsTagFragment extends SearchResultsFragment {
    List<InstagramSearchTag> tags;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InstagramClient instagramClient = MainApplication.getInstagramClient();
              //  progress.setVisibility(ProgressBar.VISIBLE);
                instagramClient.getTagSearchFeed(new JsonHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                      //  progress.setVisibility(ProgressBar.GONE);
                        if (isNetworkAvailable()) {
                            Toast.makeText(mContext, R.string.network_error_message, Toast.LENGTH_SHORT);
                        } else
                            Toast.makeText(mContext, errorResponse.toString(), Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                      //  progress.setVisibility(ProgressBar.GONE);
                        tags = Utils.decodeSearchTagsFromJsonResponse(response);
                        RecyclerView rvSearchResults = (RecyclerView) mView.findViewById(R.id.rvPosts);
                        SearchTagResultsAdapter srchAdapter = new SearchTagResultsAdapter(tags);
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
    }
}
