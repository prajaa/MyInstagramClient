package com.codepath.instagram.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by prajakta on 10/27/15.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    List<InstagramPost> mPosts;
    Context context;
    float aspectRatio;

    public PostsAdapter(List<InstagramPost> posts) {
        this.mPosts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

//        parent.getWidth();
  //      aspectRatio = parent.getWidth()
        PostViewHolder holder = new PostViewHolder(inflater.inflate(R.layout.item_post, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        InstagramPost post = mPosts.get(position);
        Log.i("PRAJ", post.user.userName);
        holder.tvUserName.setText(post.user.userName);
        holder.profileImage.setImageURI(Uri.parse(post.user.profilePictureUrl));
        holder.mainImage.setImageURI(Uri.parse(post.image.imageUrl));
        holder.mainImage.setAspectRatio(1);
        holder.tvLikes.setText(Utils.formatNumberForDisplay(post.likesCount) + " likes");
        Log.i("PRAJ",DateUtils.getRelativeTimeSpanString(post.createdTime * 1000, System.currentTimeMillis(),DateUtils.HOUR_IN_MILLIS).toString());
        holder.tvTime.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000, System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS));
        styleCaptions(post.user.userName, post.caption, holder);

    }

    public void styleCaptions(String username, String caption, PostViewHolder holder) {
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
            holder.tvCaption.setText(ssb);
        } else {
            holder.tvCaption.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvLikes, tvCaption, tvTime;
        SimpleDraweeView mainImage, profileImage;

        public PostViewHolder(View layoutView) {
            super(layoutView);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            tvTime = (TextView) layoutView.findViewById(R.id.tvTime);
            mainImage = (SimpleDraweeView) layoutView.findViewById(R.id.my_image_view);
            profileImage = (SimpleDraweeView) layoutView.findViewById(R.id.ivUserProfile);
        }
    }


}
