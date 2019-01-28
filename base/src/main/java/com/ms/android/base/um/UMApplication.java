package com.ms.android.base.um;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.ms.android.base.BuildConfig;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import cn.sharesdk.framework.ShareSDK;


/**
 * Created by shenjb on 2017/2/22.
 * 友盟数据统计
 */

public class UMApplication extends MultiDexApplication {
    public static boolean CONFIG_UM_ENABLE = true;
    public static final int SESSION_CONTINUE_MILLIS = 1 * 2 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        //share sdk init
        ShareSDK.initSDK(this);

        if (CONFIG_UM_ENABLE) {
            //配置场景类型设置接口
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            //配置session间隔时间
            MobclickAgent.setSessionContinueMillis(SESSION_CONTINUE_MILLIS);
            MobclickAgent.openActivityDurationTrack(false);
            if (BuildConfig.DEBUG) {
                MobclickAgent.enableEncrypt(false);//6.0.0版本及以后
                MobclickAgent.setCatchUncaughtExceptions(false);
                //启动集成测试
                MobclickAgent.setDebugMode(true);
            } else {
                //启用加密
                MobclickAgent.enableEncrypt(true);//6.0.0版本及以后

//                如果开发者自己捕获了错误，需要上传到【友盟+】服务器可以调用下面方法：
//
//                public static void reportError(Context context, String error)
////或
//                public static void reportError(Context context, Throwable e)

//                //发布时启用错误信息收集
                MobclickAgent.setCatchUncaughtExceptions(false);//自己捕捉异常设false
                //关闭集成测试
                MobclickAgent.setDebugMode(false);
            }
//            String deviceInfo = getDeviceInfo(this);
//            if (!TextUtils.isEmpty(deviceInfo))
//                Logger.d(deviceInfo);
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                if (tm != null) {
                    device_id = tm.getDeviceId();
                }
            }
            String mac = null;
            FileReader fStream;
            try {
                fStream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fStream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(fStream, 1024);
                mac = in.readLine();
            } catch (IOException ignored) {
            } finally {
                try {
                    fStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}