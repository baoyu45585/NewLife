package com.ms.android.base.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ms.android.base.R;
import com.ms.android.base.utils.ActivityManager;
import com.ms.android.base.widget.CmTitleBar;
import com.ms.android.base.widget.autolayout.AutoFrameLayout;

import butterknife.ButterKnife;

/**
 * 包含通用标题栏的Activity
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/15
 */
public abstract class CmTitleBarActivity extends ShareActivity implements
        CmTitleBar.OnBackClickListener, CmTitleBar.OnRightTextClickListener, CmTitleBar.OnRightIconClickListener, CmTitleBar.OnCloseClickListener {

    private AutoFrameLayout mContainer;
    protected CmTitleBar mTitleBar;
    private boolean isForbiddenSoftInput = true;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cm_title_bar;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mContainer = (AutoFrameLayout) findViewById(R.id.container);
        mTitleBar = (CmTitleBar) findViewById(R.id.titleBar);
        mTitleBar.setOnBackClickListener(this);
        mTitleBar.setOnRightTextListener(this);
        mTitleBar.setOnRightIconListener(this);
        mTitleBar.setOnCloseClickListener(this);

        View layout = getLayoutInflater().inflate(getContentLayoutRes(), null);
        AutoFrameLayout.LayoutParams params = new AutoFrameLayout.LayoutParams(AutoFrameLayout.LayoutParams.MATCH_PARENT, AutoFrameLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        mContainer.addView(layout);

        //禁止activity创建后弹出键盘
        if (isForbiddenSoftInput)
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ButterKnife.bind(this);
        onTitleInited(savedInstanceState);
    }

    public CmTitleBar getTitleBar() {
        return mTitleBar;
    }

    public void needTitleBar(boolean needTitleBar) {
        getTitleBar().setVisibility(needTitleBar ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean needButterKnife() {
        return false;
    }

    /**
     * 返回标题栏下方的布局xml
     *
     * @return
     */
    protected abstract int getContentLayoutRes();

    /**
     * 通过标题栏初始化成功
     *
     * @param savedInstanceState
     * @return
     */
    protected abstract void onTitleInited(Bundle savedInstanceState);

    protected void setCmTitle(int titleRes) {
        mTitleBar.setTitle(titleRes);
    }

    protected void needTitleBarBackIcon(boolean needBack) {
        mTitleBar.needBackIcon(needBack);
    }

    protected void setCmTitleColor(int color) {
        mTitleBar.setTitleColor(color);
    }

    protected void setTitleBarBackground(int resid) {
        mTitleBar.setBackgroundResource(resid);
    }

    protected void setTitleBarBackIcon(int resid) {
        mTitleBar.setImgBackIcon(resid);
    }

    protected void setCmTitle(String title) {
        mTitleBar.setTitle(title);
    }

    protected void setCmTitleRightText(int textRes) {
        mTitleBar.setRightText(textRes);
    }

    protected void setCmTitleRightText(int textRes, int textColor) {
        mTitleBar.setRightText(textRes, textColor);
    }

    protected void setCmTitleRightText(String text) {
        mTitleBar.setRightText(text);
    }

    protected void setCmTitleRightIcon(int iconRes) {
        mTitleBar.setRightIcon(iconRes);
    }

    public void setWhiteTheme() {
        setStatusBarColor(R.color.white);
        mTitleBar.setWhiteTheme();
    }

    /**
     * 返回按钮的默认点击事件为finish，如果需要自定义处理，复写这个方法
     *
     * @return 返回true代表子类已处理，就不会调用finish方法
     */
    protected boolean onBackClickHandled() {
        return false;
    }

    /**
     * 标题栏右上角文字点击事件
     */
    protected void onTitleRightTextClick() {
    }

    /**
     * 标题栏右上角图标点击事件
     */
    protected void onTitleRightIconClick() {
    }

    @Override
    public void onCloseClick() {
        onTitleCloseIconClick();
    }

    protected void onTitleCloseIconClick() {
        ActivityManager.getInstance().finishActivity();
    }

    @Override
    public void onBackClick() {
        if (!onBackClickHandled()) finish();
    }

    @Override
    public void onRightTextClick() {
        onTitleRightTextClick();
    }

    @Override
    public void onRightIconClick() {
        onTitleRightIconClick();
    }

    protected void setForbiddenSoftInput(boolean forbiddenSoftInput) {
        this.isForbiddenSoftInput = forbiddenSoftInput;
    }

    protected boolean isForbiddenSoftInput() {
        return isForbiddenSoftInput;
    }
}

