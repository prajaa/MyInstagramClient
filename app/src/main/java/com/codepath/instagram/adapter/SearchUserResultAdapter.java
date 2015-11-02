package com.codepath.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by prajakta on 10/30/15.
 */
public class SearchUserResultAdapter extends RecyclerView.Adapter<SearchUserResultAdapter.SearchUserViewHolder> {

    List<InstagramUser> mUsers;
    Context mContext;

    public SearchUserResultAdapter(List<InstagramUser> mUsers) {
        this.mUsers = mUsers;
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        SearchUserViewHolder holder = new SearchUserViewHolder(inflater.inflate(R.layout.item_search, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, int position) {
        InstagramUser user = mUsers.get(position);
        holder.tvUserName.setText(user.userName);
        holder.tvUserDetails.setText(user.fullName);
        holder.ivUserProfile.setImageURI(Uri.parse(user.profilePictureUrl));

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class SearchUserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserDetails;
        SimpleDraweeView ivUserProfile;

        public SearchUserViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView)itemView.findViewById(R.id.tvUserName);
            tvUserDetails = (TextView) itemView.findViewById(R.id.tvUserDetail);
            ivUserProfile = (SimpleDraweeView) itemView.findViewById(R.id.ivUserProfile);

        }
    }


}
