package com.codepath.instagram.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.PhotoGridAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajakta on 11/4/15.
 */
public class ProfileFragment extends Fragment {
    Context mContext;
    String mUserId;
    List<InstagramImage> mPhotos;
    PhotoGridAdapter pgAdapter;
    TextView tvPostsCount, tvFollowersCount, tvFollowingCount;
    SimpleDraweeView ivProfile;
    String mPostsCount, mFollowersCount, mFollowingCount, mProfilePicUrl;


    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = getArguments().getString("userId");
        mPhotos = new ArrayList<InstagramImage>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);
        pgAdapter = new PhotoGridAdapter();
        pgAdapter.notifyDataSetChanged();
        rvPhotos.setAdapter(pgAdapter);
        tvPostsCount = (TextView) view.findViewById(R.id.tvPostsCount);
        tvFollowersCount = (TextView) view.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount);
        ivProfile = (SimpleDraweeView) view.findViewById(R.id.ivProfile);
        rvPhotos.setLayoutManager(new GridLayoutManager(mContext, 3));
        getPhotos(mUserId);
        getFollowersCount(mUserId);
        getFollowingCount(mUserId);
       // super.onViewCreated(view, savedInstanceState);
    }

    private void getFollowingCount(String userId) {
        InstagramClient client = MainApplication.getInstagramClient();
        client.getFollowingFeed(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                List<InstagramUser> users = Utils.decodeUsersFromJsonResponse(response);
                mFollowingCount = users.size() + "";
                tvFollowingCount.setText(mFollowingCount);
                Log.i("PROFILE following count", mFollowingCount);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                mFollowingCount = "0";
                tvFollowingCount.setText(mFollowingCount);

            }
        }, userId);

    }

    private void getFollowersCount(String userId) {
        InstagramClient client = MainApplication.getInstagramClient();
        client.getFollowersFeed(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                List<InstagramUser> users = Utils.decodeUsersFromJsonResponse(response);
                mFollowersCount = users.size() + "";
                tvFollowersCount.setText(mFollowersCount);
                Log.i("PROFILE followers count", mFollowersCount);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                mFollowersCount = "0";
                tvFollowersCount.setText(mFollowersCount);
            }
        }, userId);
    }


    private void getPhotos(String userId) {
        InstagramClient client = MainApplication.getInstagramClient();
        client.getUsersPhotoFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               // super.onSuccess(statusCode, headers, response);
                pgAdapter.clear();
             //   Log.i("PROFILE",response.toString());
                List<InstagramPost> posts =  Utils.decodePostsFromJsonResponse(response);
                InstagramImage image;
                mPostsCount = posts.size() + "";
                tvPostsCount.setText(mPostsCount);
                for (int i = 0 ; i < posts.size() ; i++) {
                  //  Log.i("PROFILE", posts.get(i).image.imageUrl);
                    if (i == 0)
                        mProfilePicUrl = posts.get(i).user.profilePictureUrl;

                    mPhotos.add(posts.get(i).image);
                }
                ivProfile.setImageURI(Uri.parse(mProfilePicUrl));
                pgAdapter.addAll(mPhotos);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "Error "+errorResponse, Toast.LENGTH_SHORT);
            }
        }, userId);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
