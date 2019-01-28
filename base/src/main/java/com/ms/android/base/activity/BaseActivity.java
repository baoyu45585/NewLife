package com.ms.android.base.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ms.android.base.R;
import com.ms.android.base.utils.ActivityManager;
import com.ms.android.base.utils.AndroidVersionCheckUtils;
import com.ms.android.base.utils.KeyBoardUtil;
import com.ms.android.base.utils.statusbar.OSUtil;
import com.ms.android.base.utils.statusbar.StatusBarUtilRomUI;
import com.ms.android.base.utils.task.TaskHelper;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * @author zhangky@chinasunfun.com
 * @since 2017/2/16
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int INVALID_COLOR = -100;
    private boolean autoClearEdittextFocus = true;

    /**
     * 布局res文件，需要的参数为R.layout.xxx，和调用setContentView(R.layout.xxx)效果一致
     *
     * @return
     */
    protected abstract int getLayoutRes();

    protected abstract boolean needButterKnife();

    /**
     * 设置状态栏颜色，需要的参数为R.color.xxx
     *
     * @return colorRes
     */
    protected int getStatusBarColor() {
        return INVALID_COLOR;
    }

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    protected OSUtil.ROM_TYPE mRomType = OSUtil.ROM_TYPE.OTHER_ROM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preOnCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(getLayoutRes());
        if (needButterKnife()) {
            ButterKnife.bind(this);
        }
        mRomType = OSUtil.getRomType();
        setStatusBarColor (getStatusBarColor() == INVALID_COLOR ? R.color.colorPrimary : getStatusBarColor());
        init(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (UMApplication.CONFIG_UM_ENABLE) {
//            MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//            MobclickAgent.onResume(this);          //统计时长
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (UMApplication.CONFIG_UM_ENABLE) {
//            MobclickAgent.onPageEnd(this.getClass().getSimpleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//            MobclickAgent.onPause(this);
//        }
    }

    protected void preOnCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 禁止横屏

//        mTintManager = new SystemBarTintManager(this);
//        mTintManager.setStatusBarTintEnabled(true);
//        TypedArray typedArray = getTheme().obtainStyledAttributes(ATTRS);
//        int color = typedArray.getColor(0, getResources().getColor(R.color.rgb_eb2d23));
//        typedArray.recycle();
//        mTintManager.setStatusBarTintColor(color);
    }

    @SuppressLint("NewApi")
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (AndroidVersionCheckUtils.hasIcecreamsandwich()) {
            ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
            content.getChildAt(0).setFitsSystemWindows(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (AndroidVersionCheckUtils.hasIcecreamsandwich()) {
            view.setFitsSystemWindows(true);
        }
        super.setContentView(view, params);
    }

    @SuppressLint("NewApi")
    @Override
    public void setContentView(View view) {
        if (AndroidVersionCheckUtils.hasIcecreamsandwich()) {
            view.setFitsSystemWindows(true);
        }
        super.setContentView(view);
    }

    /**
     * 当用户离开edittext区域后自动取消焦点
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoClearEdittextFocus) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v != null && v instanceof EditText) {
                    Rect outRect = new Rect();
                    v.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                        KeyBoardUtil.closeKeybord((EditText) v, this);
                        v.clearFocus();
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private View mLoadingView;
    private ImageView ivAnim;

    public void showLoading() {
        showLoading("");
    }

    public void showLoading(String loadMsg) {
        View contentView = findViewById(android.R.id.content);
        if (contentView != null && contentView instanceof FrameLayout) {
            if (mLoadingView == null) {
                mLoadingView = View.inflate(this, R.layout.common_loading_img, null);
            }
            ivAnim = (ImageView) mLoadingView.findViewById(R.id.ivAnim);
            TextView tvLoadMsg = (TextView) mLoadingView.findViewById(R.id.tvLoadMsg);
            tvLoadMsg.setText(loadMsg);
            tvLoadMsg.setVisibility(TextUtils.isEmpty(loadMsg) ? View.GONE : View.VISIBLE);
//            AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getDrawable();
//            animationDrawable.start();
            Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.loading);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            ivAnim.startAnimation(operatingAnim);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLoadingView.setLayoutParams(params);
            ViewParent viewParent = mLoadingView.getParent();
            if (viewParent != null) {
                ((FrameLayout) viewParent).removeView(mLoadingView);
            }
            ((FrameLayout) contentView).addView(mLoadingView);
        }
    }

    public void dismissLoading() {
        final View contentView = findViewById(android.R.id.content);
        if (contentView != null && contentView instanceof FrameLayout) {
            TaskHelper.post(new Runnable() {
                @Override
                public void run() {
                    if (ivAnim != null) {
//                AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getDrawable();
//                animationDrawable.stop();
                        ivAnim.clearAnimation();
                        ((FrameLayout) contentView).removeView(mLoadingView);
                    }
                }
            });
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorRes
     */
    protected void setStatusBarColor(int colorRes) {
        int color = getResources().getColor(colorRes);
        if (color == Color.WHITE) {//白色
            StatusBarUtil.setColor(this, color, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //标题栏亮色模式--黑色字体
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if (mRomType == OSUtil.ROM_TYPE.MIUI_ROM) {//适配MIUI
                StatusBarUtilRomUI.StatusBarLightMode(this, StatusBarUtilRomUI.TYPE_MIUI);
            } else if (mRomType == OSUtil.ROM_TYPE.FLYME_ROM) {//适配FLYME
                StatusBarUtilRomUI.StatusBarLightMode(this, StatusBarUtilRomUI.TYPE_FLYME);
            } else {//统一设置半透明模板
                StatusBarUtil.setTranslucent(this);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            StatusBarUtil.setColor(this, color, 0);
            //适配MIUI和FLYME
            if (mRomType == OSUtil.ROM_TYPE.MIUI_ROM) {
                StatusBarUtilRomUI.StatusBarDarkMode(this, StatusBarUtilRomUI.TYPE_MIUI);
            } else if (mRomType == OSUtil.ROM_TYPE.FLYME_ROM) {
                StatusBarUtilRomUI.StatusBarDarkMode(this, StatusBarUtilRomUI.TYPE_FLYME);
            }
        }
    }

    @Override
    public void finish() {
        ActivityManager.getInstance().removeActivity(this);
        super.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        new AppSettingsDialog.Builder(this).build().show();
    }



    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();

    }
}
