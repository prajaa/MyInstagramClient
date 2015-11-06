package com.codepath.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajakta on 11/4/15.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder> {

    List<InstagramImage> mPhotos;
    Context mContext;

    public PhotoGridAdapter() {
        mPhotos = new ArrayList<InstagramImage>();
    }

    @Override
    public PhotoGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        PhotoGridViewHolder pgHolder = new PhotoGridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gridphotos, parent, false));
        return pgHolder;
    }

    @Override
    public void onBindViewHolder(PhotoGridViewHolder holder, int position) {
        InstagramImage photo = mPhotos.get(position);
        holder.ivPhoto.setImageURI(Uri.parse(photo.imageUrl));
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void clear() {
        if (mPhotos != null) {
            mPhotos.clear();
            notifyDataSetChanged();
        }
    }

    // Add a list of items
    public void addAll(List<InstagramImage> photos) {
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    public class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivPhoto;

        public PhotoGridViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (SimpleDraweeView) itemView.findViewById(R.id.ivPhoto);
        }
    }

}


