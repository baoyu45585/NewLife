package com.ms.android.newlife;

import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ms.android.base.network.ApiService;
import com.ms.android.base.network.Env;
import com.ms.android.base.um.UMApplication;
import com.ms.android.base.utils.SDUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class AppNewLife extends UMApplication {

    private Env dev = Env.DEV;
    private  static AppNewLife instance ;
    public static String SDPath ; //sd卡地址
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();


    }

    private void init() {
        SDPath = SDUtil.getDiskCacheDir(instance);//获取路径
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.LOG_DEBUG;//检查日志是否该被打印
            }
        });
        ButterKnife.setDebug(BuildConfig.LOG_DEBUG);
        switchEnv();
        ApiService.getInstance().switchEnv(dev);//初始化域名
        if (BuildConfig.DEBUG){
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }


    public static AppNewLife getInstance() {
        return instance;
    }


    private void switchEnv() {
        switch (BuildConfig.BUILD_TYPE) {
            case "debug"://开发环境
                dev = Env.DEV;
                break;
            case "release"://线上环境
                dev = Env.ONLINE;
                break;
        }
    }
}
