package com.ms.android.base.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ms.android.base.R;


/**
 * 通用对话框，UI效果参考
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/23
 */
public class CmDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_CONTENT = "extra_content";
    private static final String EXTRA_ASSIST = "extra_assist";
    private static final String EXTRA_PRIMARY = "extra_primary";

    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvContent2;
    private TextView mTvAssistButton;
    private TextView mTvPrimaryButton;
    private View mLayoutTitleContent;
    private View mDividerButton;

    /**
     * @param title   标题
     * @param content 内容
     * @param assist  辅助操作
     * @param primary 主操作
     * @return
     */
    public static CmDialogFragment getInstance(String title, String content, String assist, String primary) {
        CmDialogFragment fragment = new CmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_CONTENT, content);
        bundle.putString(EXTRA_ASSIST, assist);
        bundle.putString(EXTRA_PRIMARY, primary);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_rectangle_white_corner_20px);
            //设置对话框按屏幕宽度的75%
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = inflater.inflate(R.layout.cm_dialog_fragment, container, false);
        mLayoutTitleContent = inflate.findViewById(R.id.layout_title_content);
        mTvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
        mTvContent2 = (TextView) inflate.findViewById(R.id.tv_content2);
        mTvAssistButton = (TextView) inflate.findViewById(R.id.tv_bottom_assist);
        mTvPrimaryButton = (TextView) inflate.findViewById(R.id.tv_bottom_primary);
        mDividerButton = inflate.findViewById(R.id.divider_bottom);

        mTvAssistButton.setOnClickListener(this);
        mTvPrimaryButton.setOnClickListener(this);


        String title = getArguments().getString(EXTRA_TITLE);
        String content = getArguments().getString(EXTRA_CONTENT);
        String assist = getArguments().getString(EXTRA_ASSIST);
        String primary = getArguments().getString(EXTRA_PRIMARY);

        if (TextUtils.isEmpty(title)) {
            mLayoutTitleContent.setVisibility(View.GONE);
            mTvContent2.setVisibility(View.VISIBLE);
            mTvContent2.setText(TextUtils.isEmpty(content) ? "" : content);
        } else {
            mTvTitle.setText(title);
            mTvContent.setText(TextUtils.isEmpty(content) ? "" : content);
        }
        if (TextUtils.isEmpty(assist)) {
            mTvAssistButton.setVisibility(View.GONE);
            mDividerButton.setVisibility(View.GONE);
        } else {
            mTvAssistButton.setText(assist);
        }
        mTvPrimaryButton.setText(TextUtils.isEmpty(primary) ? "" : primary);

        return inflate;
    }

    private OnPrimaryClickListener mPriListener;
    private OnAssistClickListener mAssListener;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_bottom_primary) {
            if (mPriListener != null) mPriListener.onPrimaryClick();
            else dismiss();
        } else if (id == R.id.tv_bottom_assist) {
            if (mAssListener != null) mAssListener.onAssistClick();
            else dismiss();
        }
    }

    public interface OnPrimaryClickListener {
        void onPrimaryClick();
    }

    public interface OnAssistClickListener {
        void onAssistClick();
    }

    /**
     * 右边按钮的监听
     * @param listener
     */
    public void setOnPrimaryClickListener(OnPrimaryClickListener listener) {
        mPriListener = listener;
    }

    /**
     * 左边按钮的监听
     * @param listener
     */
    public void setOnAssistClickListener(OnAssistClickListener listener) {
        mAssListener = listener;
    }



    private void test() {
        String title = "标题";
        String content = "内容";
        String cancel ="取消";
        String confirm ="确定";
        final CmDialogFragment dialogFragment = CmDialogFragment.getInstance(title, content, cancel, confirm);
        dialogFragment.setOnPrimaryClickListener(new CmDialogFragment.OnPrimaryClickListener() {
            @Override
            public void onPrimaryClick() {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getChildFragmentManager(), "");
    }
}
