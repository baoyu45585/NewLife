package com.ms.android.update.response;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.ms.android.update.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


/**
 * @author csx
 */
public class DownloadIntentService extends IntentService {
    /**
     * 默认超时时间
     */
    private static final int DEFAULT_TIME_OUT = 10 * 1000;
    /**
     * 缓存大小
     */
    private static final int BUFFER_SIZE = 10 * 1024;

    public static final String INTENT_VERSION_NAME = "service.intent.version_name";
    public static final String INTENT_DOWNLOAD_URL = "service.intent.download_url";
    public static final String SAVE_FILE_NAME = "ms.apk";

    private static final int NOTIFICATION_ID = UUID.randomUUID().hashCode();

    private String mDownloadUrl;
    private String mVersionName;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;


    public DownloadIntentService() {
        super("DownloadService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadIntentService(String name) {
        super(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 移除通知
        mNotifyManager.cancel(NOTIFICATION_ID);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mVersionName = intent.getStringExtra(INTENT_VERSION_NAME);
            mDownloadUrl = intent.getStringExtra(INTENT_DOWNLOAD_URL);
            if (!TextUtils.isEmpty(mVersionName) && !TextUtils.isEmpty(mDownloadUrl)) {
                    initNotify();
                startDownload();
            }
        }
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            channel.enableLights(false); //是否在桌面icon右上角展示小红点
//            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
//            channel.enableVibration(false);
//            channel.setSound(null, null);
//            mNotificationManager.createNotificationChannel(channel);
//        }


        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(String.format("%s V%s", getString(getApplicationInfo().labelRes), mVersionName))
                //通知首次出现在通知栏，带上升动画效果的
                .setTicker(getString(R.string.mishi_new_versions))
                //通常是用来表示一个后台任务
                .setOngoing(true)
                //通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setWhen(System.currentTimeMillis());

        //解决5.0系统通知栏白色Icon的问题
        Drawable appIcon = getAppIcon(this);
        Bitmap drawableToBitmap = null;
        if (appIcon != null) {
            drawableToBitmap = drawableToBitmap(appIcon);
        }
        if (drawableToBitmap != null) {
            mBuilder.setSmallIcon(R.drawable.app_update_icon);
            mBuilder.setLargeIcon(drawableToBitmap);
        } else {
            mBuilder.setSmallIcon(getApplicationInfo().icon);
        }
    }









    /**
     * 开始下载
     */
    private void startDownload() {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(mDownloadUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(DEFAULT_TIME_OUT);
            urlConnection.setReadTimeout(DEFAULT_TIME_OUT);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();
            long bytetotal = urlConnection.getContentLength();
            long bytesum = 0;
            int byteread = 0;
            in = urlConnection.getInputStream();
            String apkDownLoadDir = FileManager.getApkDownLoadDir(getApplicationContext());
            File apkFile = new File(apkDownLoadDir, SAVE_FILE_NAME);
            out = new FileOutputStream(apkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            int oldProgress = 0;

            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);

                int progress = (int) (bytesum * 100L / bytetotal);
                // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
                if (progress != oldProgress) {
                    updateProgress(progress);
                }
                oldProgress = progress;
            }
            // 下载完成
            installAPk();
        } catch (Exception e) {
            if (e != null) {
                Log.e("TEST", "download apk file error:" + e.getMessage());
            } else {
                Log.e("TEST", "download apk file error:下载失败");
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {

                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {

                }
            }
        }

    }

    /**
     * 更新通知栏的进度(下载中)
     *
     * @param progress
     */
    private void updateProgress(int progress) {

            mBuilder.setContentText(String.format("正在下载:%1$d%%", progress)).setProgress(100, progress, false);
            PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent
                    .FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingintent);
            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());

    }




    /**
     * 开始安装
     */
    private void installAPk() {
        startActivity(getInstallIntent());
    }

    /**
     * 启动安装界面
     *
     * @return
     */
    private Intent getInstallIntent() {
        File apkInstallDir = FileManager.getApkInstallDir(getApplicationContext());
        Log.e("TEST", "路径---" + apkInstallDir.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apkInstallDir), "application/vnd.android.package-archive");
        } else {
            // 声明需要的零时权限
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 第二个参数，即第一步中配置的authorities
            Uri contentUri = FileProvider.getUriForFile(this,"rt.myschool.fileProvider",
                    apkInstallDir);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }
        return intent;
    }

    /**
     * 合成更新的Icon
     *
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取App的Icon
     *
     * @param context
     * @return
     */
    public Drawable getAppIcon(Context context) {
        try {
            return context.getPackageManager().getApplicationIcon(context.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
