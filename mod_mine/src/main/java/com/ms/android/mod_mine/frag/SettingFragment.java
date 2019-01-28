package com.ms.android.mod_mine.frag;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ms.android.base.ARouterStr;
import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.mod_mine.R;
import com.ms.android.mod_mine.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class SettingFragment extends BaseFragment {


    @BindView(R2.id.btn)
    Button btn;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void init(View contentView, Bundle savedInstanceState) {
        btn.setText("圣诞节啊看电视觉得");
    }

    @OnClick({R2.id.btn})
    public void onClick(View view) {
        if (view==btn){
            ARouter.getInstance().build(ARouterStr.login)
                    .withInt("type", 1)
                    .navigation(getActivity(), 666);

        }

    }

}
