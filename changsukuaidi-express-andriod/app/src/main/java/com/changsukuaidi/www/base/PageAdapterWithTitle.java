package com.changsukuaidi.www.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class PageAdapterWithTitle<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> fragments;
    private String[] title;

    public PageAdapterWithTitle(FragmentManager fm, List<T> fragments, String[] title) {
        super(fm);
        this.fragments = fragments;
        this.title = title;
    }

    @Override
    public T getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
