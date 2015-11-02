package com.codepath.instagram.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.instagram.models.InstagramSearchTag;

import java.util.List;

/**
 * Created by prajakta on 10/31/15.
 */
public class SearchTagResultsAdapter extends SearchAdapter<InstagramSearchTag> {

    List<InstagramSearchTag> mTags;

    public SearchTagResultsAdapter(List<InstagramSearchTag> items) {
        super(items);
        mTags = items;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.VH holder, int position) {
        InstagramSearchTag stag = mTags.get(position);
        holder.tvName.setText(stag.tag);
        holder.tvDetails.setText(stag.count+"");
        holder.ivProfile.setVisibility(View.GONE);
       // holder.tvName.setLayoutParams();
    }



}
