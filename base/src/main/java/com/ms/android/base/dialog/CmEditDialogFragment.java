package com.ms.android.base.dialog;

import android.app.Dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ms.android.base.R;
import com.ms.android.base.widget.ClearEditText;
import com.ms.android.base.widget.autolayout.utils.AutoUtils;


/**
 * 通用的带有一个编辑框的对话框，参考UI：Module-Work/company- structure/新建部门.png
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/3/6
 */
public class CmEditDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_EDIT_CONTENT = "extra_content";
    private static final String EXTRA_EDIT_CONTENT_MAX_LENGTH = "extra_edit_content_max_length";
    private static final String EXTRA_EDIT_HINT = "extra_hint";
    private static final String EXTRA_EDIT_Height = "extra_height";
    private static final String EXTRA_NEGATIVE = "extra_assist";
    private static final String EXTRA_POSITIVE = "extra_primary";

    private TextView mTvTitle;
    private ClearEditText mTvContent;
    private TextView mTvAssistButton;
    private TextView mTvPrimaryButton;
    private View ll_reactangle_edit;
    private int height;

    public static class Builder {
        private Bundle mBundle;

        public Builder() {
            mBundle = new Bundle();
        }

        /**
         * 标题
         *
         * @param title 标题
         */
        public Builder setTitle(String title) {
            mBundle.putString(EXTRA_TITLE, title);
            return this;
        }

        /**
         * EditText内容
         *
         * @param content 默认显示内容
         */
        public Builder setContent(String content) {
            mBundle.putString(EXTRA_EDIT_CONTENT, content);
            return this;
        }

        /**
         * EditText内容长度限制
         *
         * @param length 文本最大长度
         */
        public Builder setContentMaxLength(int length) {
            mBundle.putInt(EXTRA_EDIT_CONTENT_MAX_LENGTH, length);
            return this;
        }

        /**
         * EditText内容长度限制
         *
         * @param height 输入框高度
         */
        public Builder setContentMinHeight(int height) {
            mBundle.putInt(EXTRA_EDIT_Height, height);
            return this;
        }

        /**
         * EditText内容提示
         *
         * @param hint 提示语
         */
        public Builder setHint(String hint) {
            mBundle.putString(EXTRA_EDIT_HINT, hint);
            return this;
        }

        public Builder setNegative(String negative) {
            mBundle.putString(EXTRA_NEGATIVE, negative);
            return this;
        }

        public Builder setPositive(String positive) {
            mBundle.putString(EXTRA_POSITIVE, positive);
            return this;
        }

        public CmEditDialogFragment build() {
            CmEditDialogFragment dialogFragment = new CmEditDialogFragment();
            dialogFragment.setArguments(mBundle);
            return dialogFragment;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.drawable.shape_rectangle_white_corner_20px);
                //设置对话框按屏幕宽度的75%
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                window.setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = inflater.inflate(R.layout.cm_edit_dialog_fragment, container, false);
        mTvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        ll_reactangle_edit = inflate.findViewById(R.id.ll_reactangle_edit);

        mTvContent = (ClearEditText) inflate.findViewById(R.id.input_content);
        mTvAssistButton = (TextView) inflate.findViewById(R.id.tv_bottom_assist);
        mTvPrimaryButton = (TextView) inflate.findViewById(R.id.tv_bottom_primary);

        String title = getArguments().getString(EXTRA_TITLE, "");
        String content = getArguments().getString(EXTRA_EDIT_CONTENT, "");
        String hint = getArguments().getString(EXTRA_EDIT_HINT, "");
        String assist = getArguments().getString(EXTRA_NEGATIVE, "");
        String primary = getArguments().getString(EXTRA_POSITIVE, "");
        int length = getArguments().getInt(EXTRA_EDIT_CONTENT_MAX_LENGTH, 0);
        height = getArguments().getInt(EXTRA_EDIT_Height, 0);

        mTvTitle.setText(title);
        mTvContent.setText(TextUtils.isEmpty(content) ? "" : content);
        mTvContent.setHint(TextUtils.isEmpty(hint) ? "" : hint);
        if (length > 0) {
            InputFilter[] fa = new InputFilter[1];
            fa[0] = new InputFilter.LengthFilter(length);//长度限制
            mTvContent.setFilters(fa);
        }
        mTvContent.setSelection(mTvContent.getText().length());
        if (TextUtils.isEmpty(assist)) {
            mTvAssistButton.setVisibility(View.GONE);
        } else {
            mTvAssistButton.setText(assist);
        }
        mTvPrimaryButton.setText(TextUtils.isEmpty(primary) ? "" : primary);
//        mTvContent.addTextChangedListener(new ButtonState());
        mTvPrimaryButton.setOnClickListener(this);
        mTvAssistButton.setOnClickListener(this);
        if (height > 0) {
            mTvContent.setMinHeight(AutoUtils.getPercentHeightSize(height));
            mTvContent.setGravity(Gravity.TOP | Gravity.START);
            mTvContent.setSingleLine(false);
            mTvContent.setMaxLines(5);
            mTvContent.setMaxLines(7);
        } else {
            mTvContent.setSingleLine(true);
        }

        return inflate;
    }

    private OnClickListener onClickListener;

    @Override
    public void onClick(View v) {
        if (v == mTvPrimaryButton) {
            if (onClickListener != null) {
                onClickListener.onPrimaryClick(mTvContent.getText().toString().trim());
            } else {
                dismiss();
            }
        } else if (v == mTvAssistButton) {
            if (onClickListener != null) {
                onClickListener.onAssistClick();
            } else {
                dismiss();
            }
        }
    }

    public TextView getTitle() {
        return mTvTitle;
    }

    public ClearEditText getContent() {
        return mTvContent;
    }

    public TextView getAssistButton() {
        return mTvAssistButton;
    }

    public TextView getPrimaryButton() {
        return mTvPrimaryButton;
    }

    /**
     * 点击事件回调
     */
    public interface OnClickListener {
        /**
         * 重要按钮点击（确定）
         *
         * @param content
         */
        void onPrimaryClick(String content);

        /**
         * 二级点击（取消）
         */
        void onAssistClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    private class ButtonState implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(mTvContent.getText())) {
                mTvContent.setGravity(Gravity.CENTER);
                ll_reactangle_edit.setBackgroundResource(R.drawable.shape_stroke_rgb_01_a8);
            } else {
                mTvContent.setGravity(height > 0 ? Gravity.CENTER_VERTICAL | Gravity.TOP : Gravity.CENTER_VERTICAL | Gravity.START);
                ll_reactangle_edit.setBackgroundResource(R.drawable.shape_stroke_primary);
            }
        }
    }


    public void test(String content) {
        CmEditDialogFragment.Builder builder = new CmEditDialogFragment.Builder();
        builder.setHint("请输入什么")
                .setPositive("确定")
                .setNegative("取消")
                .setTitle("标题")
                .setContentMinHeight(160)
                .setContentMaxLength(25)
                .setContent(content);
        final CmEditDialogFragment dialogFragment = builder.build();
        dialogFragment.show(getChildFragmentManager(), CmEditDialogFragment.class.getSimpleName());
        dialogFragment.setOnClickListener(new CmEditDialogFragment.OnClickListener() {
            @Override
            public void onPrimaryClick(String content) {
                dialogFragment.dismiss();
            }

            @Override
            public void onAssistClick() {
                dialogFragment.dismiss();
            }
        });
    }
}
