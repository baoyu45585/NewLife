
package com.ms.android.update.response;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.ms.android.update.R;


import java.io.File;

/**
 * 版本更新工具类
 *
 * @author kaiyuan.zhang
 * @since Oct 16, 2015
 */
public class UpdateUtil {

    private Context mContext;
    private DownloadReceiver mDownloadReceiver;
    private File mDownloadedApk;

    public UpdateUtil(Context context) {
        mContext = context;
    }

    /**
     * 下载apk
     *
     * @param url      下载地址
     * @param dirPath  下载后apk存放的路径
     * @param fileName 下载后apk的文件名
     */
    public void downloadApk(String url, String dirPath, String fileName) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(dirPath) || TextUtils.isEmpty(fileName)) {
            return;
        }
     //   Logger.d("url=" + url + "，dirPath=" + dirPath + "，fileName=" + fileName);
        mDownloadedApk = new File(dirPath, fileName);
        if (mDownloadedApk.exists()) {
            mDownloadedApk.delete();//删除旧的同名apk
        }

        DownloadManager downloadManager = (DownloadManager) mContext
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setTitle(getAppLabel() + mContext.getString(R.string.newest_version)).setDestinationUri(Uri.fromFile(new File(dirPath, fileName)))
                .setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        if (mDownloadReceiver == null) {
            mDownloadReceiver = new DownloadReceiver();
        }
        mContext.registerReceiver(mDownloadReceiver, filter);
    }

    private String getAppLabel() {
        int labelRes = mContext.getApplicationInfo().labelRes;
        return mContext.getString(labelRes);
    }

    private class DownloadReceiver extends BroadcastReceiver {

        private boolean isCanInstall(Intent intent) {
            return intent != null && intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    && intent.getPackage().equals(mContext.getPackageName()) && mDownloadedApk != null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
         //   Logger.d("intent=" + intent);
            if (isCanInstall(intent)) {
                Intent promptInstall = new Intent(Intent.ACTION_VIEW).setDataAndType(
                        Uri.parse("file://" + mDownloadedApk.getAbsolutePath()),
                        "application/vnd.android.package-archive");
                promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(promptInstall);
                if (mDownloadReceiver != null) {
                    mContext.unregisterReceiver(mDownloadReceiver);
                    mDownloadReceiver = null;
                }
            }
        }

    }


    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
