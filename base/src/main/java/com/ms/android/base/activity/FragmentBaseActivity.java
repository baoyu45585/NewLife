package com.ms.android.base.activity;

import android.os.Bundle;

import com.ms.android.base.R;
import com.ms.android.base.fragment.BaseFragment;

/**
 * 无头部的片段Activity
 *
 * @author chendw@chinasunfun.com
 * @since 2017/8/29
 */
public abstract class FragmentBaseActivity extends ShareActivity {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_frag_container;
    }

    @Override
    protected boolean needButterKnife() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, getAttachFragment())
                .commitAllowingStateLoss();
        init();
    }

    public abstract BaseFragment getAttachFragment();

    public abstract void init();
}
