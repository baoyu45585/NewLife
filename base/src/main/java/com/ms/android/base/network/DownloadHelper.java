package com.ms.android.base.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ms.android.base.R;
import com.ms.android.base.network.repo.ProfileAPI;
import com.ms.android.base.utils.ImageCache;
import com.ms.android.base.utils.ImageFileUtils;
import com.ms.android.base.utils.MD5Utils;
import com.ms.android.base.utils.SDUtil;
import com.ms.android.base.utils.task.TaskHelper;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import framework.utils.FileUtil.FileUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Wenxc on 2017/3/21.
 * 下载文件工具类
 */
public class DownloadHelper {
    private static DownloadHelper instance;
    private static final String DOWNLOAD_FILE_PATH = File.separator + "download";

    private DownloadHelper() {
    }

    public static DownloadHelper getInstance() {
        if (instance == null)
            instance = new DownloadHelper();
        return instance;
    }

    //流就绪回调
    interface StreamReadyCallback {
        /**
         * 流就绪
         *
         * @param stream 文件流
         */
        void streamReady(InputStream stream);

        void fail(Throwable throwable);
    }

    //流就绪回调
    public interface DownLoadCallback {
        void success();

        void fail(Throwable throwable);
    }

    /**
     * 此方法耗时，要求异步线程处理
     *
     * @param url      请求地址
     * @param callback 流回调
     */
    private void getInputStream(Context context, String url, boolean cache, StreamReadyCallback callback) {
        if (context == null || TextUtils.isEmpty(url) || callback == null) return;
        //判断是否有缓存数据，存在则读取缓存数据
        boolean forceOverride = true;
        if (cache) {
            forceOverride = false;
            String md5Hash = MD5Utils.MD5Hash(url);
            File externalCacheDir = context.getApplicationContext().getExternalCacheDir();
            if (externalCacheDir != null) {
                String cacheFilePath = externalCacheDir.getAbsolutePath();
                File md5File = new File(cacheFilePath + DOWNLOAD_FILE_PATH, md5Hash);
                if (md5File.exists()) {
                    try {
                        callback.streamReady(new FileInputStream(md5File));
                        forceOverride = false;
                    } catch (FileNotFoundException e) {
                        //文件存储异常，读取网络资源
                    }
                }
            } else {
                callback.fail(new Exception("file dir not exist,can cache data into externalCacheDir."));
            }
        }
        if (forceOverride) return;
        try {
            URL innerUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) innerUrl.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            InputStream inputStream = connection.getInputStream();
            callback.streamReady(inputStream);
        } catch (IOException e) {
            callback.fail(e);
        }
    }

    /**
     * 保存本地
     * @param body
     * @return
     */
    private  boolean writeResponseBodyToDisk(ResponseBody body, String file, String filename) {
        try {
            // todo change the file location/name according to your needs
            File flieMulu = new File(file);
            if(!flieMulu.exists()){
                flieMulu.mkdirs();
            }
            File f = new File(file, filename);
            if (f.exists()) {
                f.delete();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(f);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 不重复下载图片
     * @param context
     * @param url
     * @param path
     * @param filepath
     */
    public void noRepeatDownImage(final Context context, String url,  final String path, final String filepath){
        if (context == null || TextUtils.isEmpty(url)|| TextUtils.isEmpty(path)|| TextUtils.isEmpty(filepath)) return;
        File f = new File(path, filepath);
        if (!ImageFileUtils.isImageExist(f.getPath())){
            downFile(context, url, path, filepath, new DownLoadCallback() {
                @Override
                public void success() {

                }

                @Override
                public void fail(Throwable throwable) {

                }
            });
        }
    }


    /**
     * 此方法耗时，要求异步线程处理
     * @param context
     * @param url
     * @param path
     * @param filepath
     * @param callback
     */
    public void downFile(Context context, String url,  final String path, final String filepath, final DownLoadCallback callback) {
        if (context == null || TextUtils.isEmpty(url)|| TextUtils.isEmpty(path)|| TextUtils.isEmpty(filepath)) return;
        File flieMulu = new File(path);
        if(!flieMulu.exists()){
            flieMulu.mkdirs();
        }
        final File f = new File(path, filepath);
        if (f.exists()) {
            f.delete();
        }
        getInputStream(context, url, true, new StreamReadyCallback() {
            @Override
            public void streamReady(InputStream stream) {
                try {
                    FileUtil.writeFile( f.getPath(), stream);
                }catch (Exception e){
                    if (callback != null) callback.fail(e);
                }
                if (callback != null) callback.success();
            }

            @Override
            public void fail(Throwable throwable) {
                if (callback != null) callback.fail(throwable);
            }
        });
    }



    /**
     * 不重复下载图片
     */
    public void noRepeatDownImage(final String url, final String path, final String filepath){
        if (TextUtils.isEmpty(url))return;
        File f = new File(path, filepath);
        if (!ImageFileUtils.isImageExist(f.getPath())){
            downFile(url, path, filepath, new DownLoadCallback() {
                @Override
                public void success() {

                }

                @Override
                public void fail(Throwable throwable) {

                }
            });
        }
    }

    /**
     * 下载文件(大型图片不成功概率很大)
     * http://wallpapers1.hellowallpaper.com/art_black-wallpaper--01_04-1280x800.jpg
     * @param url 图片网络路径
     * @param path  下载目录
     * @param filepath  文件名
     * @param callback 下载回调
     */
    public void downFile(final String url, final String path, final String filepath,final DownLoadCallback callback) {
        if (TextUtils.isEmpty(url))return;
        Call<ResponseBody> call = ApiService.getInstance().getProfileLoadAPI(ProfileAPI.class).downloadFileWithDynamicUrlSync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 Logger.d("下载类型：",""+response.headers().get("Content-Type"));
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(),path,filepath);
                    if (writtenToDisk){
                        if (callback != null) callback.success();
                    }else{
                        if (callback != null) callback.fail(new Throwable("写入失败~"));
                    }
                } else {
                    if (callback != null) callback.fail(new Throwable("请求失败~"));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.e("加载图片失败"+t.toString());
                if (callback != null) callback.fail(t);
            }
        });
    }





/**
 * 使用Glide
 */

    public static void loadImage(Context mContext,ImageView imageView, String url){
        String path = SDUtil.getDiskCacheDir(mContext)+"/";//获取路径
        setImageView(mContext,imageView,url, path,0,null);
    }

    public static void loadImage(Context mContext,ImageView imageView, String url,String path){
        setImageView(mContext,imageView,url, path,0,null);
    }

    public static void loadImage(Context mContext,ImageView imageView, String url,String path,int resId){
        setImageView(mContext,imageView,url, path,resId,null);
    }

    public static void loadImage(Context mContext,ImageView imageView, String url,int resId){
        String path = SDUtil.getDiskCacheDir(mContext)+"/";//获取路径
        setImageView(mContext,imageView,url, path,resId,null);
    }

    public static void loadImageDef(Context mContext,ImageView imageView, String url){
        String path = SDUtil.getDiskCacheDir(mContext)+"/";//获取路径
        setImageView(mContext,imageView,url, path,0,null);
    }

    public static void loadQinNiuImageDef(Context mContext,ImageView imageView, String url,String qinniu){
        String path = SDUtil.getDiskCacheDir(mContext)+"/";//获取路径
        setImageView(mContext,imageView,url, path,0,qinniu);
    }
    public static void loadImageDef(Context mContext,ImageView imageView, String url,String path){
        setImageView(mContext,imageView,url, path,0,null);
    }





    /**
     *
     * @param url
     * @param path
     */
    public static void setImageView(Context mContext,final ImageView imageView, String url, final String path,int resId,String qinniu) {

        if (TextUtils.isEmpty(url)){
            if (resId!=0){
                imageView.setImageResource(resId);
            }
            return;
        }
        //文件名
        final String fileName =url.substring(url.lastIndexOf("/")+1);
        File f = new File(path, fileName);

//        if (!ImageFileUtils.isImageExist(f.getPath())){
//
//        }
        Bitmap bitmap = ImageCache.getInstance().get(path+fileName);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        if (resId!=0){
            imageView.setImageResource(resId);
        }
        if (f.exists()) {
            Glide.with(mContext).asBitmap().load(path+fileName).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    imageView.setImageBitmap(resource);
                    ImageCache.getInstance().put(path+fileName, resource);
                }
            });
        }else{
           if (!TextUtils.isEmpty(qinniu)){
               url=url+qinniu;
           }
            Glide.with(mContext).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    //    imageView.setImageResource(resId);
                    return false;
                }

                @Override
                public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ImageCache.getInstance().put(path+fileName, resource);
                    imageView.setImageBitmap(resource);
                    TaskHelper.run(new Runnable() {
                        @Override
                        public void run() {
                           saveFile(resource, path,fileName);
                        }
                    });
                    return false;
                }
            }).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                }
            });
        }
    }

    public static String getQinLiuImage(int w,int h){
        return"?imageMogr2/strip|imageView2/1/w/"+w+"/h/"+h+"/q/80/size-limit/25k!";
    }
    public static String getQinLiuImage(){
        return "?imageMogr2/size-limit/25k!";
    }

    /**
     * 保存图片在本地
     * @param bitmap
     * @param filename
     */
    public static void saveFile(Bitmap bitmap,String file, String filename) {
        File flieMulu = new File(file);
        if(!flieMulu.exists()){
            flieMulu.mkdirs();
        }
        File f = new File(file, filename);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    Request request = new Request.Builder().url(url).build();
//    OkHttpClient mOkHttpClient = new OkHttpClient();
//        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
//        @Override
//        public void onFailure(okhttp3.Call call, IOException e) {
//
//        }
//
//        @Override
//        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//
//        }
//    });

    //        if (TextUtils.isEmpty(urlContent))return;
//        final String html = urlContent.replaceAll("\\\\", "").trim();
//        htmlSpanner = new HtmlSpanner(getContext(), AutoLayoutConifg.getInstance().getScreenWidth(), new Handler());
//        TaskHelper.run(new Runnable() {
//            @Override
//            public void run() {
//                spannable =  htmlSpanner.fromHtml(html);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvWeb.setText(spannable);
//                    }
//                });
//            }
//        });

}
