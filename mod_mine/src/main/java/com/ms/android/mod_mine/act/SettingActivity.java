package com.ms.android.mod_mine.act;

import android.content.Intent;

import com.ms.android.base.activity.FragmentTitleActivity;
import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.base.widget.CmToast;
import com.ms.android.mod_mine.frag.SettingFragment;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class SettingActivity extends FragmentTitleActivity {


    @Override
    public BaseFragment getAttachFragment() {
        return new SettingFragment();
    }

    @Override
    public void init() {
        setCmTitle("设置");
        doShareWithType();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 666:

                if (data!=null)
                    data.getIntExtra("type",0);
                CmToast.show(this,"666测试成功");
                break;
            default:
                break;
        }
    }
}
