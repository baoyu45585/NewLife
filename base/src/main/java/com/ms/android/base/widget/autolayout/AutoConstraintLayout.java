package com.ms.android.base.widget.autolayout;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

/**
 * @author zhangky@chinasunfun.com
 * @since 2017/8/24
 */
public class AutoConstraintLayout extends ConstraintLayout {

    public AutoConstraintLayout(Context context) {
        super(context);
    }

    public AutoConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
