package com.ms.android.base.dialog;



import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.ms.android.base.R;


/**
 * Created by Wenxc on 2017/3/2.
 * 分享对话框
 */

public class SharePlatformDialog extends DialogFragment implements View.OnClickListener{
    public enum Platform {
        WEICHAT, WEIFRIEND, QQ, SINA_WEIBO, RENREN, EMAIL, SMS, COPY
    }

    private PlatformSelectedListener selectedListener;

    public interface PlatformSelectedListener {
        void onPlatformSelected(Platform platform);
    }

    public PlatformSelectedListener getSelectedListener() {
        return selectedListener;
    }

    public void setSelectedListener(PlatformSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager
     */
    public void show(FragmentManager fragmentManager) {
        if (isAdded())return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(this, SharePlatformDialog.class.getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.show(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window == null) return;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
        mWindowAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(mWindowAttributes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = inflater.inflate(R.layout.frag_share_platform_dialog, container, false);
        inflate.findViewById(R.id.rl_all).setOnClickListener(this);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        inflate.findViewById(R.id.ll_weifriend).setOnClickListener(this);
        inflate.findViewById(R.id.ll_weichat).setOnClickListener(this);
        inflate.findViewById(R.id.ll_qq).setOnClickListener(this);
        inflate.findViewById(R.id.ll_sina_weibo).setOnClickListener(this);
        inflate.findViewById(R.id.ll_renren).setOnClickListener(this);
        inflate.findViewById(R.id.ll_email).setOnClickListener(this);
        inflate.findViewById(R.id.ll_sms).setOnClickListener(this);
        inflate.findViewById(R.id.ll_copy).setOnClickListener(this);

        return inflate;
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.rl_all || i == R.id.btn_cancel) {
            dismiss();

        } else if (i == R.id.ll_weifriend) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.WEIFRIEND);
            dismiss();

        } else if (i == R.id.ll_weichat) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.WEICHAT);
            dismiss();

        } else if (i == R.id.ll_qq) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.QQ);
            dismiss();

        } else if (i == R.id.ll_sina_weibo) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.SINA_WEIBO);
            dismiss();

        } else if (i == R.id.ll_renren) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.RENREN);
            dismiss();

        } else if (i == R.id.ll_email) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.EMAIL);
            dismiss();

        } else if (i == R.id.ll_sms) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.SMS);
            dismiss();

        } else if (i == R.id.ll_copy) {
            if (selectedListener != null)
                selectedListener.onPlatformSelected(Platform.COPY);
            dismiss();

        }
    }
}
