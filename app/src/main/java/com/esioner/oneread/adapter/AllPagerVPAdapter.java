package com.esioner.oneread.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Esioner on 2018/6/23.
 */

public class AllPagerVPAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    //    private String[] tabName = {"专题", "作者", "摄影", "阅读", "连载", "音乐", "影视"};
    private String[] tabName = {"阅读", "音乐", "影视"};

    public AllPagerVPAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabName[position];
    }
}
