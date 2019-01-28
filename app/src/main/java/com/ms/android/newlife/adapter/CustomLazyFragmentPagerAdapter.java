package com.ms.android.newlife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.base.widget.lazy.LazyFragmentPagerAdapter;
import com.ms.android.newlife.fragment.BeeCoinsFragment;
import com.ms.android.newlife.fragment.MainFragment;
import com.ms.android.newlife.fragment.MineFragment;
import com.ms.android.newlife.fragment.TaskFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomLazyFragmentPagerAdapter extends LazyFragmentPagerAdapter {
    List<BaseFragment> fragments = new ArrayList<>();

    public CustomLazyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new MainFragment());
        fragments.add(new TaskFragment());
        fragments.add(new BeeCoinsFragment());
        fragments.add(new MineFragment());

    }

    @Override
    public Fragment getItem(ViewGroup container, int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}