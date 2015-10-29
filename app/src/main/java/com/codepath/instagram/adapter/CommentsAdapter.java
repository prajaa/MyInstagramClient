package com.codepath.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by prajakta on 10/29/15.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    List<InstagramComment> mComments;
    Context context;


    public CommentsAdapter(List<InstagramComment> comments) {
        this.mComments = comments;
    }


    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        CommentsViewHolder cvHolder = new CommentsViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
        return cvHolder;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        InstagramComment comment = mComments.get(position);
        holder.ivUser.setImageURI(Uri.parse(comment.user.profilePictureUrl));
        holder.tvDuration.setText(DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        Utils.styleText(comment.user.userName, comment.text, holder.tvComment, context);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivUser;
        TextView tvComment, tvDuration;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            ivUser = (SimpleDraweeView) itemView.findViewById(R.id.ivCommentUserProfile);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);

        }
    }

}
