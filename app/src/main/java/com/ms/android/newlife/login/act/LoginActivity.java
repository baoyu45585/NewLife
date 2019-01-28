package com.ms.android.newlife.login.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ms.android.base.ARouterStr;
import com.ms.android.base.activity.CmTitleBarActivity;
import com.ms.android.base.widget.CmToast;
import com.ms.android.newlife.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 *
 * @author chendw@chinasunfun.com
 * @since 2017/8/23
 */
@Route(path = ARouterStr.login)
public class LoginActivity extends CmTitleBarActivity {


    @Override
    protected int getContentLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onTitleInited(Bundle savedInstanceState) {
        int type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            CmToast.show(this, "传送成功~");
        }
    }

    @OnClick({R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:{
               setResult(666,new Intent().putExtra("type",6));
               finish();
                break;
            }
            default:
                break;
        }

    }
}
