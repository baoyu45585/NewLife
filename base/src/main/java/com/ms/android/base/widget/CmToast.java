package com.ms.android.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.android.base.R;

/**
 * 通用Toast工具
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/18
 */
public class CmToast {
    private static Toast toast;
    private static TextView toastText;

    private CmToast() {
        throw new AssertionError();
    }

    public static void show(Context context, int resId) {
        if (context == null) return;
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        if (context == null) return;
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        if (context == null) return;
        show(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
        if (context == null) return;
        show(context.getApplicationContext(), text, duration, Gravity.CENTER);
    }

    public static void show(Context context, CharSequence text, int duration, int gravity) {
        if (TextUtils.isEmpty(text)) return;
        if (context == null) return;
        if (toastText == null) toast = null;
        if (toast == null) toast = createCustomToast(context);
        toast.setGravity(gravity, 0, 0);
        toastText.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    private static Toast createCustomToast(Context context) {
        Toast toast = new Toast(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.common_toast, null, false);
        toastText = (TextView) inflate.findViewById(R.id.tv_toast);
        toast.setView(inflate);
        return toast;
    }

    public static void show(Context context, int resId, Object... args) {
        if (context == null) return;
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        if (context == null) return;
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
