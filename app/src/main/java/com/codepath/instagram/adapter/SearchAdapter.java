package com.codepath.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by prajakta on 10/31/15.
 */
public abstract class SearchAdapter<E> extends RecyclerView.Adapter<SearchAdapter.VH> {

    List<E> items;
    Context mContext;

    public SearchAdapter(List<E> items) {
        this.items = items;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        VH holder = new VH(inflater.inflate(R.layout.item_search, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        SimpleDraweeView ivProfile;

        public VH(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvDetails = (TextView) itemView.findViewById(R.id.tvUserDetail);
            ivProfile = (SimpleDraweeView) itemView.findViewById(R.id.ivUserProfile);
        }


    }




}
