package com.ms.android.newlife;

import android.os.Bundle;
import android.view.View;

import com.ms.android.base.activity.ShareActivity;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class ShareDemoActivity extends ShareActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.demo_share_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShareWithType();
            }
        });
    }


    @Override
    public boolean needButterKnife() {
        return false;
    }
}
