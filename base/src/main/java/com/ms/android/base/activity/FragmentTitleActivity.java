package com.ms.android.base.activity;

import android.os.Bundle;

import com.ms.android.base.R;
import com.ms.android.base.fragment.BaseFragment;

/**
 * Created by Wenxc on 2017/8/29.
 * 片段Activity
 */

public abstract class FragmentTitleActivity extends CmTitleBarActivity {
    protected int getContentLayoutRes() {
        return R.layout.activity_frag_container;
    }

    public abstract BaseFragment getAttachFragment();

    @Override
    protected void onTitleInited(Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, getAttachFragment())
                .commitAllowingStateLoss();
        init();
    }

    public abstract void init();
}
