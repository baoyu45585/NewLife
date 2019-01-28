//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ms.android.base.utils;

import android.os.Build.VERSION;

public class AndroidVersionCheckUtils {
    private AndroidVersionCheckUtils() {
    }

    public static boolean hasDonut() {
        return VERSION.SDK_INT >= 4;
    }

    public static boolean hasEclair() {
        return VERSION.SDK_INT >= 5;
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= 8;
    }

    public static boolean hasGingerbread() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return VERSION.SDK_INT >= 12;
    }

    public static boolean hasIcecreamsandwich() {
        return VERSION.SDK_INT >= 14;
    }
}
