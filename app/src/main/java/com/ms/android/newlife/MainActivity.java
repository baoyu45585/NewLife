package com.ms.android.newlife;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ms.android.base.activity.BaseActivity;
import com.ms.android.base.widget.MyTabHost;
import com.ms.android.base.widget.lazy.LazyViewPager;
import com.ms.android.newlife.adapter.CustomLazyFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    LazyViewPager lazyViewPager;
    @BindView(R.id.tab_host)
    MyTabHost tabHost;

    private ArrayList<String> names;
    private ArrayList<Integer> iconResId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean needButterKnife() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        lazyViewPager.setAdapter(new CustomLazyFragmentPagerAdapter(getSupportFragmentManager()));
        lazyViewPager.setOffscreenPageLimit(3);
        lazyViewPager.addOnPageChangeListener(this);
        names = new ArrayList<>();
        names.add("商城");
        names.add("任务");
        names.add("蜂币");
        names.add("我的");
        iconResId = new ArrayList<>();
        iconResId.add(R.mipmap.ic_tab_index_selected);
        iconResId.add(R.mipmap.ic_tab_index_selected);
        iconResId.add(R.mipmap.ic_tab_index_selected);
        iconResId.add(R.mipmap.ic_tab_index_selected);
        tabHost.setup(names, iconResId);
        tabHost.setTabSelectedChange(new MyTabHost.TabSelectedChange() {
            @Override
            public void onTabSelectChange(int index) {
                lazyViewPager.setCurrentItem(index, false);
            }
        });
        tabHost.setCurrentTab(0);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
