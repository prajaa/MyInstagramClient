package com.codepath.instagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.HomeActivity;
import com.codepath.instagram.adapter.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.adapter.SearchFragmentStatePagerAdapter;

/**
 * Created by prajakta on 11/1/15.
 */
public class SearchFragment  extends Fragment{

    Context mContext;
    View mView;
    private ProgressBar progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        progress = (ProgressBar) view.findViewById(R.id.progress);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        viewPager.setAdapter(new SearchFragmentStatePagerAdapter(getChildFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs2);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }




}
