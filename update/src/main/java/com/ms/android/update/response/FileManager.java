package com.ms.android.update.response;

import android.content.Context;
import android.os.Environment;

import java.io.File;


/**
 * Created by csx on 2017/12/21.
 */

public class FileManager {

    private final static String APK_PATH = "apk";

    /**
     * 获取APK的下载目录
     *
     * @return
     */
    public static String getApkDownLoadDir(Context context) {
        String apkPaht = getCacheDirectory(context) + File.separator + APK_PATH;
        return FileUtil.createDirFile(apkPaht).getPath();
    }

    /**
     * 获取APK的安装目录
     *
     * @return
     */
    public static File getApkInstallDir(Context context) {
        String apkPahtName = getCacheDirectory(context) + File.separator + APK_PATH;
        File apkFile = new File(FileUtil.createDirFile(apkPahtName), DownloadIntentService.SAVE_FILE_NAME);
        if (!apkFile.exists()) {
            return null;
        }
        return apkFile;
    }

    /**
     * 创建缓存（Android/data/com.ishangbin.user/cache目录下）
     *
     * @return
     */
    public static String getCacheDirectory(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (cachePath == null) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }
        return cachePath;
    }

}
