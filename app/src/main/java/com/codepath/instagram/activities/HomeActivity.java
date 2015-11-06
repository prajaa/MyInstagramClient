package com.codepath.instagram.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.codepath.instagram.R;
import com.codepath.instagram.adapter.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.adapter.PostsAdapter;
import com.codepath.instagram.fragments.NetworkDialogFragment;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.ProfileFragment;
import com.codepath.instagram.helpers.InstagramClient;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

//import cz.msebera.android.httpclient.Header;


public class HomeActivity extends AppCompatActivity implements PostsFragment.OnItemSelectedListener {
    /**
     * Day 1. Created the Home activity using the popular.json
     * Day 2. Instead of using a prepopulated file get the real data. Use Instagram popular feeds api
     * Day 3. Use your own feed. And move the entire logic to a fragment
     * @author prajakta
     * */
    private static final String TAG = "HomeActivity";
    List<InstagramPost> posts;
    Context context;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      //  ft.replace(R.id.your_placeholder, new PostsFragment());
      //  ft.commit();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new HomeFragmentStatePagerAdapter(getSupportFragmentManager(),
                HomeActivity.this));


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setFitsSystemWindows(true);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

    }

    /*
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
        }

        //Day 2 - Fetch the Posts from Instagram API
        //Day 3 - change the Instagram client to be a Rest client
        InstagramClient instagramClient = new InstagramClient(context);
        instagramClient.getSelfFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                posts = Utils.decodePostsFromJsonResponse(response);
                RecyclerView rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
                SimpleVerticalSpacerItemDecoration rvSeparation = new SimpleVerticalSpacerItemDecoration(24);
                rvPosts.addItemDecoration(rvSeparation);
                PostsAdapter postsAdapter = new PostsAdapter(posts);
                rvPosts.setAdapter(postsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
                NetworkDialogFragment networkDialogFragment = new NetworkDialogFragment();
                FragmentManager fm = getFragmentManager();
                networkDialogFragment.show(fm, "Network Dialog");
                Log.i(TAG, "Reaching network error");
                Toast.makeText(getBaseContext(), "FAIL", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (!isNetworkAvailable()) {
                    NetworkDialogFragment networkDialogFragment = new NetworkDialogFragment();
                    FragmentManager fm = getFragmentManager();
                    networkDialogFragment.show(fm, "Network Dialog");
                } else {

                }

            }
        });

        return posts;
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
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

    @Override
    public void onProfilePicItemSelected(String userId) {
        FragmentTransaction fts;
       // Log.i("PRAJ clicked user id", mPosts.get(position).user.userName);
        fts = getSupportFragmentManager().beginTransaction();
      //  fts = getChildFragmentManager().beginTransaction();
        ProfileFragment pfFragment = ProfileFragment.newInstance(userId);
        fts.addToBackStack("posts");
        fts.replace(R.id.viewpager, pfFragment);
        fts.commit();
    }
}
