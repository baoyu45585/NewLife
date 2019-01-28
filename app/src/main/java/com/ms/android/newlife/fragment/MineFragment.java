package com.ms.android.newlife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.base.widget.lazy.LazyFragmentPagerAdapter;
import com.ms.android.mod_mine.act.SettingActivity;
import com.ms.android.newlife.R;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的页面
 *
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class MineFragment extends BaseFragment implements LazyFragmentPagerAdapter.Laziable {
    @BindView(R.id.btn)
    Button btn;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View contentView, Bundle savedInstanceState) {
        Logger.e(this.getClass().getName());
    }


    @OnClick({R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:{
                Intent intent=new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

    }
}
