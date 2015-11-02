package com.codepath.instagram.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.SearchResultUserFragment;
import com.codepath.instagram.fragments.SearchResultsTagFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by prajakta on 11/1/15.
 */
public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    String[] titles = {"USERS", "TAGS"};

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(position) {
            case 0:
                fragmentClass = SearchResultUserFragment.class;
                break;
            case 1:
                fragmentClass = SearchResultsTagFragment.class;
                break;
            default:
                fragmentClass = SearchResultUserFragment.class;
                break;
        }

        try {
            fragment = (Fragment)fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
