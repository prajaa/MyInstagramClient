package com.codepath.instagram.helpers;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapter.PostsAdapter;
import com.codepath.instagram.fragments.NetworkDialogFragment;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

//import cz.msebera.android.httpclient.Header;

public class Utils {
    private static final String TAG = "Utils";
    private static final NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
    }

    public static String formatNumberForDisplay(int number) {
        return numberFormat.format(number);
    }

    public static JSONObject loadJsonFromAsset(Context context, String fileName) throws IOException, JSONException {
        InputStream inputStream = context.getResources().getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        JSONObject result = new JSONObject(builder.toString());

        inputStream.close();
        bufferedReader.close();

        return result;
    }

    public static List<InstagramPost> decodePostsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramPost> posts = InstagramPost.fromJson(getDataJsonArray(jsonObject));
        return posts == null ? new ArrayList<InstagramPost>() : posts;
    }

    public static List<InstagramComment> decodeCommentsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramComment> comments = InstagramComment.fromJson(getDataJsonArray(jsonObject));
        return comments == null ? new ArrayList<InstagramComment>() : comments;
    }

    public static List<InstagramUser> decodeUsersFromJsonResponse(JSONObject jsonObject) {
        List<InstagramUser> users = InstagramUser.fromJson(getDataJsonArray(jsonObject));
        return users == null ? new ArrayList<InstagramUser>() : users;
    }

    public static List<InstagramSearchTag> decodeSearchTagsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramSearchTag> searchTags = InstagramSearchTag.fromJson(getDataJsonArray(jsonObject));
        return searchTags == null ? new ArrayList<InstagramSearchTag>() : searchTags;
    }

    private static JSONArray getDataJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        if (jsonObject != null) {
            jsonArray = jsonObject.optJSONArray("data");
        }
        return jsonArray;
    }


    /**
     * General method to style text in the format <BLUE>Username</BLUE>  <GRAY>Rest of the Text</GRAY>
     * */
    public static TextView styleText(String username, String caption, TextView textView, Context context) {

        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.blue_text));
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableStringBuilder ssb = new SpannableStringBuilder(username);
        ssb.setSpan(
                blueForegroundColorSpan,            // the span to add
                0,                                 // the start of the span (inclusive)
                ssb.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(bss, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(" ");
        ForegroundColorSpan grayColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text));
        if (caption != null) {
            ssb.append(caption);
            ssb.setSpan(
                    grayColorSpan,            // the span to add
                    ssb.length() - caption.length(),
                    // the start of the span (inclusive)
                    ssb.length(),                      // the end of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ssb);
        } else {
            textView.setVisibility(View.GONE);
        }

        return textView;

    }





}
