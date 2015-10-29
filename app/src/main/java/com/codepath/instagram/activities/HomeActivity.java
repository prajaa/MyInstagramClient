package com.codepath.instagram.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.PostsAdapter;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    List<InstagramPost> posts;
    Context context;
   // private static final String MOCK_DATA_FILE = ""

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        List<InstagramPost> posts = getPosts();
        context = this;
     /*   RecyclerView rvPosts = (RecyclerView)findViewById(R.id.rvPosts);
        SimpleVerticalSpacerItemDecoration rvSeparation = new SimpleVerticalSpacerItemDecoration(24);
        rvPosts.addItemDecoration(rvSeparation);
        PostsAdapter postsAdapter = new PostsAdapter(posts);
        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));*/



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
        InstagramClient.getPopularFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               posts = Utils.decodePostsFromJsonResponse(response);
                RecyclerView rvPosts = (RecyclerView)findViewById(R.id.rvPosts);
                SimpleVerticalSpacerItemDecoration rvSeparation = new SimpleVerticalSpacerItemDecoration(24);
                rvPosts.addItemDecoration(rvSeparation);
                PostsAdapter postsAdapter = new PostsAdapter(posts);
                rvPosts.setAdapter(postsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getBaseContext(), "FAIL", Toast.LENGTH_LONG).show();
            }
        });

        return posts;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
