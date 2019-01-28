package com.ms.android.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ms.android.base.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment<T> extends Fragment {

    protected Context mContext = null;
    protected T mActivityCallBack = null;
    View rootView = null;
    boolean hasAttach = false;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        hasAttach = true;
        mContext = activity;
        try {
            mActivityCallBack = (T) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isAttach() {
        return hasAttach;
    }

    @Override
    public void onDetach() {
        mContext = null;
        hasAttach = false;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = getLayoutRes();
        if (layoutRes != 0) {
            rootView = inflater.inflate(layoutRes, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
//        setupFragmentComponent(HuhaApp.get(mContext).getAppComponent());
        init(rootView, savedInstanceState);
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (UMApplication.CONFIG_UM_ENABLE)
//            MobclickAgent.onPageEnd(this.getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (UMApplication.CONFIG_UM_ENABLE)
//            MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public T getActivityCallback() {
        return mActivityCallBack;
    }


    private View mLoadingView;

    protected void showLoading() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showLoading();

    }

    protected void dismissLoading() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).dismissLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    /**
     * getLayout id for fragment
     *
     * @return layout id
     */
    public abstract int getLayoutRes();

    protected abstract void init(View contentView, Bundle savedInstanceState);

}