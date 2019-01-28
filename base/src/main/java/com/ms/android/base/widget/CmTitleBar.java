package com.ms.android.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.android.base.R;
import com.ms.android.base.widget.autolayout.AutoLinearLayout;

/**
 * 通用标题栏
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/15
 */
public class CmTitleBar extends AutoLinearLayout implements View.OnClickListener {

    private ImageView mImgBack;
    private TextView mTvTitle;
    private TextView mRightText;
    private ImageView mRightIcon;
    private TextView mCloseText;

    public CmTitleBar(Context context) {
        this(context, null);
    }

    public CmTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.common_title_bar, this);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mRightText = (TextView) findViewById(R.id.tvRightText);
        mRightIcon = (ImageView) findViewById(R.id.imgRightIcon);
        mCloseText = (TextView) findViewById(R.id.txtClose);
        mImgBack.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mRightIcon.setOnClickListener(this);
        mCloseText.setOnClickListener(this);
    }

    public void setRightEnable(boolean enable) {
        mRightText.setEnabled(enable);
        mRightIcon.setEnabled(enable);
    }

    public void setCloseVisible(boolean visible) {
        mCloseText.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setTitle(int titleRes) {
        mTvTitle.setText(titleRes);
    }

    public void setTitleColor(int color) {
        mTvTitle.setTextColor(color);
    }

    public void setImgBackIcon(int resId) {
        mImgBack.setImageResource(resId);
    }

    public void needBackIcon(boolean needBack) {
        mImgBack.setVisibility(needBack ? View.VISIBLE : View.GONE);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setRightText(int textRes) {
        mRightText.setVisibility(VISIBLE);
        mRightText.setText(textRes);
        mRightIcon.setVisibility(GONE);
    }

    public void setRightText(String text) {
        mRightText.setVisibility(VISIBLE);
        mRightText.setText(text);
        mRightIcon.setVisibility(GONE);
    }

    public void setRightText(int textRes, int textColor) {
        setRightText(textRes);
        mRightText.setTextColor(getResources().getColor(textColor));
    }

    public void setRightText(String text, int textColor) {
        setRightText(text);
        mRightText.setTextColor(getResources().getColor(textColor));
    }

    public void setRightIcon(int iconRes) {
        mRightIcon.setVisibility(VISIBLE);
        mRightIcon.setImageResource(iconRes);
        mRightText.setVisibility(GONE);
    }

    public void setWhiteTheme() {
        setBackgroundColor(getResources().getColor(R.color.white));
        mImgBack.setImageResource(R.mipmap.ic_back_black);
        int color01 = getResources().getColor(R.color.rgb_01);
        mCloseText.setTextColor(color01);
        mTvTitle.setTextColor(color01);
        mRightText.setTextColor(color01);
    }

    public TextView getRightText() {
        return mRightText;
    }

    @Override
    public void onClick(View v) {
        if (v == mImgBack) {
            if (mBackListener != null) mBackListener.onBackClick();
        } else if (v == mRightText) {
            if (mRightTextListener != null) mRightTextListener.onRightTextClick();
        } else if (v == mRightIcon) {
            if (mRightIconListener != null) mRightIconListener.onRightIconClick();
        } else if (v == mCloseText) {
            if (onCloseClickListener != null) {
                onCloseClickListener.onCloseClick();
            }
        }
    }

    private OnBackClickListener mBackListener;
    private OnRightTextClickListener mRightTextListener;
    private OnRightIconClickListener mRightIconListener;
    private OnCloseClickListener onCloseClickListener;

    public void setOnBackClickListener(OnBackClickListener listener) {
        mBackListener = listener;
    }

    public void setOnRightTextListener(OnRightTextClickListener listener) {
        mRightTextListener = listener;
    }

    public void setOnRightIconListener(OnRightIconClickListener listener) {
        mRightIconListener = listener;
    }

    public void setOnCloseClickListener(OnCloseClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;
    }

    public interface OnCloseClickListener {
        void onCloseClick();
    }

    public interface OnBackClickListener {
        void onBackClick();
    }

    public interface OnRightIconClickListener {
        void onRightIconClick();
    }

    public interface OnRightTextClickListener {
        void onRightTextClick();
    }
}
