package com.codepath.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by prajakta on 10/27/15.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    List<InstagramPost> mPosts;
    Context context;

    public PostsAdapter(List<InstagramPost> posts) {
        this.mPosts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
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
        Log.i("PRAJ", DateUtils.getRelativeTimeSpanString(post.createdTime * 1000, System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS).toString());
        holder.tvTime.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        styleText(post.user.userName, post.caption, holder.tvCaption);
        insertComments(holder, post.comments, post.commentsCount);

    }

    private void insertComments(PostViewHolder holder, List<InstagramComment> comments, int commentCount) {
        holder.llComents.removeAllViews();
        String userName, userComment;
        TextView commentView;
        holder.llComents.removeAllViews();
        switch (commentCount) {
            case 0: holder.llComents.setVisibility(View.GONE); break;
            case 1:
                userName = comments.get(0).user.userName;
                userComment = comments.get(0).text;
                commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComents, false).findViewById(R.id.tvComment);
                styleText(userName, userComment, commentView);
                holder.llComents.addView(commentView);
                break;
            case 2:
                commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComents, false).findViewById(R.id.tvComment);
                for (InstagramComment comment : comments) {
                    userName = comment.user.userName;
                    userComment = comment.text;
                    commentView = styleText(userName, userComment, commentView);
                    holder.llComents.addView(commentView);
                }
                break;
            default:
                holder.llComents.removeAllViews();
                for (int i = 0 ; i < 2; i++) {
                    commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComents, false);
                    userName = comments.get(i).user.userName;
                    userComment = comments.get(i).text;
                    commentView = styleText(userName, userComment, commentView);
                    holder.llComents.addView(commentView);
                }
                break;
        }

    }

    /**
     * General method to style text in the format <BLUE>Username</BLUE>  <GRAY>Rest of the Text</GRAY>
     * */
    public TextView styleText(String username, String caption, TextView textView) {

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

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvLikes, tvCaption, tvTime;
        SimpleDraweeView mainImage, profileImage;
        LinearLayout llComents;

        public PostViewHolder(View layoutView) {
            super(layoutView);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            tvTime = (TextView) layoutView.findViewById(R.id.tvTime);
            mainImage = (SimpleDraweeView) layoutView.findViewById(R.id.my_image_view);
            profileImage = (SimpleDraweeView) layoutView.findViewById(R.id.ivUserProfile);
            llComents = (LinearLayout) layoutView.findViewById(R.id.llcomments);
        }
    }


}
