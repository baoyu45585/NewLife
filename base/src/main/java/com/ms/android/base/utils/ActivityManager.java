package com.ms.android.base.utils;

import android.app.Activity;
import android.content.Context;

import java.util.List;
import java.util.Stack;

public class ActivityManager {

    private static ActivityManager instance;
    public Stack<Activity> activityStack;

    private ActivityManager() {
        activityStack = new Stack<Activity>();
    }

    /**
     * 单例
     */
    public synchronized static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.push(activity);
    }


    /**
     * 判断是否含有这个类
     */
    public boolean contains(Class<?> cls) {
        List<Activity> activityStacks = activityStack;
        for (Activity activity : activityStacks) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前Activity（堆顶元素）
     */
    public Activity currentActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.peek();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (!activityStack.isEmpty()) {
            activityStack.pop().finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity == null) return;
        removeActivity(activity);
        activity.finish();
        activity = null;
    }

    public void removeActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    public void pushToFront(Activity activity) {
        if (activity == null || (activityStack.size() > 0 && activityStack.peek() == activity))
            return;

        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
        activityStack.push(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Stack<Activity> activitys = new Stack<Activity>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activitys.add(activity);
            }
        }

        for (Activity activity : activitys) {
            finishActivity(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!activityStack.isEmpty()) {
            finishActivity();
        }
    }

    private boolean isExistCls(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 回到某个Activity
     *
     * @param cls
     */
    public boolean backToActivity(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        if (!isExistCls(cls)) {
            return false;
        }
        while (true) {
            Activity act = currentActivity();
            if (act == null || act.getClass().equals(cls)) {
                break;
            }
            finishActivity();
        }
        return true;
    }



    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            int pid = Process.myPid();
//            Process.killProcess(pid);
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
