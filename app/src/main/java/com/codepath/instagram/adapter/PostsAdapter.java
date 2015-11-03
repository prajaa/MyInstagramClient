package com.codepath.instagram.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by prajakta on 10/27/15.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    List<InstagramPost> mPosts;
    Context context;



    public PostsAdapter() {
        mPosts = new ArrayList<InstagramPost>();
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

        if (post.commentsCount > 2) {
            holder.tvViewAllComment.setText("View all "+post.commentsCount+" comments");
            holder.tvViewAllComment.setVisibility(View.VISIBLE);
        } else {
            holder.tvViewAllComment.setVisibility(View.GONE);
        }

        Utils.styleText(post.user.userName, post.caption, holder.tvCaption, context);

        if ( post.comments != null && post.comments.size() > 0)
            insertComments(holder, post.comments);

    }

    public void clear() {
        if (mPosts != null) {
            mPosts.clear();
            notifyDataSetChanged();
        }
    }

    // Add a list of items
    public void addAll(List<InstagramPost> posts) {
        mPosts.addAll(posts);
        notifyDataSetChanged();
    }


    private void insertComments(PostViewHolder holder, List<InstagramComment> comments) {
        holder.llComents.removeAllViews();
        String userName, userComment;
        TextView commentView;
        holder.llComents.removeAllViews();
        switch (comments.size()) {
            case 0: holder.llComents.setVisibility(View.GONE); break;
            case 1:
                userName = comments.get(0).user.userName;
                userComment = comments.get(0).text;
                commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComents, false);
                holder.llComents.removeAllViews();
                Utils.styleText(userName, userComment, commentView, context);
                holder.llComents.addView(commentView);
                break;
            case 2:

                holder.llComents.removeAllViews();
                for (InstagramComment comment : comments) {
                    commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComents, false);
                    userName = comment.user.userName;
                    userComment = comment.text;
                    commentView = Utils.styleText(userName, userComment, commentView, context);
                    holder.llComents.addView(commentView);
                }
                break;
            default:
                holder.llComents.removeAllViews();
                for (int i = 0 ; i < 2; i++) {
                    commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComents, false);
                    userName = comments.get(i).user.userName;
                    userComment = comments.get(i).text;
                    commentView = Utils.styleText(userName, userComment, commentView, context);
                    holder.llComents.addView(commentView);
                }
                break;
        }

    }

    /**
     * General method to style text in the format <BLUE>Username</BLUE>  <GRAY>Rest of the Text</GRAY>
     *
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
*/
    @Override
    public int getItemCount() {
        if (mPosts != null)
            return mPosts.size();
        else
            return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUserName, tvLikes, tvCaption, tvTime, tvViewAllComment;
        SimpleDraweeView mainImage, profileImage;
        LinearLayout llComents;
        ImageButton ibShare;

        public void showPopUp(final String imageUrl, View v) {

            PopupMenu popup = new PopupMenu(context, v);
            // Inflate the menu from xml
            popup.getMenuInflater().inflate(R.menu.post_dots, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.mShare) {
                        Uri imageUri = Uri.parse(imageUrl);

                        ImagePipeline imagePipeline = Fresco.getImagePipeline();
                        ImageRequest imageRequest = ImageRequestBuilder
                                .newBuilderWithSource(imageUri)
                                .setRequestPriority(Priority.HIGH)
                                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                                .build();

                        DataSource<CloseableReference<CloseableImage>> dataSource =
                                imagePipeline.fetchDecodedImage(imageRequest, context);

                        try {
                            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                @Override
                                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                    if (bitmap == null) {
                                        Log.d("PRAJ", "Bitmap data source returned success, but bitmap null.");
                                        return;
                                    }
                                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                            bitmap, "Image Description", null);
                                    Uri bmpUri = Uri.parse(path);
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                    shareIntent.setType("image/*");

                                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                                }

                                @Override
                                public void onFailureImpl(DataSource dataSource) {
                                    // No cleanup required here
                                }
                            }, CallerThreadExecutor.getInstance());
                        } finally {
                            if (dataSource != null) {
                                dataSource.close();
                            }
                        }
                    }
                    return true;
                }
            });

            popup.show();




        }

        public PostViewHolder(View layoutView) {
            super(layoutView);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            tvTime = (TextView) layoutView.findViewById(R.id.tvTime);
            mainImage = (SimpleDraweeView) layoutView.findViewById(R.id.my_image_view);
            profileImage = (SimpleDraweeView) layoutView.findViewById(R.id.ivUserProfile);
            llComents = (LinearLayout) layoutView.findViewById(R.id.llcomments);
            tvViewAllComment = (TextView) layoutView.findViewById(R.id.tvViewAllComments);
            ibShare = (ImageButton) layoutView.findViewById(R.id.ibShare);
            ibShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    showPopUp(mPosts.get(position).image.imageUrl, v);
                }
            });
            tvViewAllComment.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            InstagramPost post = mPosts.get(position);
            String mediaId = post.mediaId;
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra("mediaId", mediaId);
            context.startActivity(intent);

        }
    }


}
