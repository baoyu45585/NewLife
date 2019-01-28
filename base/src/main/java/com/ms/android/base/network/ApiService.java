package com.ms.android.base.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;


import com.ms.android.base.utils.MD5Utils;
import com.ms.android.base.utils.NetWorkUtils;


import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shenjb on 2017/8/24.
 * 职秘请求数据中心
 */
public class ApiService {
    private static final String BASE_URL_DEV = "";//测试域名
    private static final String BASE_URL_ONLINE = "";//线上
//    private static final String BASE_URL_ONLINE = "";//五缘
    private static final String AGENT = "";//每个版本预设用户名
    private static final String SALT = "";//每个版本预设密码
    private static final String AGENT_21 = "";//每个版本预设用户名
    private static final String SALT_21 = "";//每个版本预设密码
    private String baseUrl = BASE_URL_DEV;
    public static String  clientIp ;

    private ApiService() {
    }

    private static ApiService instance;

    public static ApiService getInstance() {
        if (instance == null) instance = new ApiService();
        return instance;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void switchEnv(Env env) {
        switch (env) {
            case DEV:
                baseUrl = BASE_URL_DEV;
                break;
            case ONLINE:
                baseUrl = BASE_URL_ONLINE;
                break;
        }
    }

    public <T> T getProfileAPI(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getClient()).build();
        return retrofit.create(service);
    }

    /**
     * 下载
     * @param service
     * @param <T>
     * @return
     */
    public <T> T getProfileLoadAPI(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getLoadClient()).build();
        return retrofit.create(service);
    }


    public OkHttpClient getClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
//        // 添加通用的Header
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                String url = request.url().toString();
                //the request method
                String method = request.method();
                Request.Builder builder = request.newBuilder();
                if (TextUtils.isEmpty(clientIp)){
                    try {
                        clientIp = NetWorkUtils.getLocalMacAddressFromIp(); //获取mac地址
                    } catch (Exception e) {
                        e.printStackTrace();
                        clientIp = "";
                    }
                }
                builder.addHeader("Token", clientIp);
                url =url.replace(baseUrl,"");
                builder.addHeader("User-Agent", AGENT_21);
                String s= MD5Utils.MD5Hash(url+SALT_21);
                builder.addHeader("Hash",s);


                return chain.proceed(builder.build());
            }
        });
        okHttpBuilder.connectTimeout(15, TimeUnit.SECONDS);//链接超时
        okHttpBuilder.readTimeout(10, TimeUnit.SECONDS);//读取超时
        okHttpBuilder.retryOnConnectionFailure(true);//失败重连
        okHttpBuilder.writeTimeout(10, TimeUnit.SECONDS);//写入超时
        okHttpBuilder.addNetworkInterceptor(new LogInterceptor());
//        if (BuildConfig.DEBUG) {
//            //日志拦截器
//            okHttpBuilder.addNetworkInterceptor(new LogInterceptor());
//        }
        return okHttpBuilder.build();
    }


    public OkHttpClient getLoadClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
        okHttpBuilder.connectTimeout(15, TimeUnit.SECONDS);//链接超时
        okHttpBuilder.readTimeout(10, TimeUnit.SECONDS);//读取超时
        okHttpBuilder.retryOnConnectionFailure(true);//失败重连
        okHttpBuilder.writeTimeout(10, TimeUnit.SECONDS);//写入超时
        okHttpBuilder.addNetworkInterceptor(new LogInterceptor());
//        if (BuildConfig.DEBUG) {
//            //日志拦截器
//            okHttpBuilder.addNetworkInterceptor(new LogInterceptor());
//        }
        return okHttpBuilder.build();
    }




    public class LogInterceptor implements Interceptor {
        static final String TAG = "LogInterceptor.java";

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            //the request url
            String url = request.url().toString();
            //the request method
            String method = request.method();
            long t1 = System.nanoTime();
       //     Logger.d(TAG, String.format(Locale.getDefault(), "Sending %s request [url = %s]", method, url));
            //the request body
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                StringBuilder sb = new StringBuilder("Request Body [");
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                sb.append("]");
      //          Log.d(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            //the response time
   //         Logger.d(TAG, String.format(Locale.getDefault(), "Received response for [url = %s] in %.1fms", url, (t2 - t1) / 1e6d));

            //the response state
     //       Logger.d(TAG, String.format(Locale.CHINA, "Received response is %s ,message[%s],code[%d]", response.isSuccessful() ? "success" : "fail", response.message(), response.code()));

            //the response data
            ResponseBody body = response.body();

            BufferedSource source;
            if (body != null) {
                source = body.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                Charset charset = Charset.defaultCharset();
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                if (charset != null) {
                    String bodyString = buffer.clone().readString(charset);
      //              Logger.d(TAG, String.format("Received response json string [%s]", bodyString));
                }
            }
            return response;
        }
    }

    /**
     * 把请求body参数值进行修改
     * 修改成json格式data{"name" :"value"}
     * data	{"mac":"84209619c72d"}
     */
    public class NetworkInterceptor implements Interceptor {
        public static final String TAG = "NetworkInterceptor.java";

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            RequestBody body = request.body();
            StringBuilder innerBuilder = new StringBuilder();
            innerBuilder.append("{");
            if (body instanceof FormBody) {
                for (int i = 0; i < ((FormBody) body).size(); i++) {
                    innerBuilder.append("\"")
                            .append(((FormBody) body).encodedName(i))
                            .append("\":\"")
                            .append(((FormBody) body).encodedValue(i))
                            .append("\",");
                }
            }
            int index = innerBuilder.lastIndexOf(",");
            if (index > 0) {
                innerBuilder.deleteCharAt(index);
            }
            innerBuilder.append("}");

            Request.Builder builder = request.newBuilder();
            builder.delete(body);
            FormBody actualBody = new FormBody.Builder().addEncoded("data", innerBuilder.toString()).build();
            builder.method(request.method(), actualBody);
            builder.header("Content-Length", String.valueOf(actualBody.contentLength()));
            String contentType = request.header("Content-Type");
            builder.header("Content-Type", contentType.concat(";charset=UTF-8"));

            CookieManager instance = CookieManager.getInstance();
            String cookie = instance.getCookie(request.url().toString());
            if (!instance.acceptCookie()) {
                instance.setAcceptCookie(true);
                instance.removeExpiredCookie();
            }
            if (cookie != null) {
                builder.header("Cookie", cookie);
            }

//            builder.addHeader("isgzip", isGzip ? "1" : "0");
//            builder.addHeader("equipmentmodel", DeviceUtils.getManufacturer());
//            builder.addHeader("equipmenttype", "android");
//            builder.addHeader("equipmentversion", DeviceUtils.getModel());
//            UserEntity userEntity = HuhaApp.this.userEntity;
//            if (userEntity != null && !userEntity.isGuest()) {
//                builder.addHeader("userid", userEntity.getUserid());
//            }
//            //包含该字符的无需上传
//            String url = request.url().toString();
//            if (!url.contains("/entry/")) {
//                String session = HuhaApp.getmApp().getSession();
//                if (!StringUtil.isEmpty(session)) {
//                    builder.addHeader("session", session);
//                }
//                builder.addHeader("version", String.valueOf(Version.getAppVersionCode(mApp)));
//                builder.addHeader("versionname", "android");
//            }
            Request build = builder.build();

            Response proceed = chain.proceed(build);
            Headers headers = proceed.headers();
            storeCookies(request.url().toString(), headers.toMultimap());

//            String session = headers.get("session");
//            if (!StringUtil.isEmpty(session)) {
//                Intent intent = new Intent();
//                intent.setAction(HttpResponseReceiver.ACTION_DISCOVER_SESSION);
//                intent.putExtra(Type.ValKey.VAL_HTTP_SESSION, session);
//                LocalBroadcastManager.getInstance(HuhaApp.getmApp()).sendBroadcast(intent);
//            }
            return proceed;
        }
    }

    /**
     * Store cookies.
     *
     * @param url   the url
     * @param heads the heads
     */
    private static void storeCookies(String url, Map<String, List<String>> heads) {
        if (url == null || heads == null) {
            return;
        }
        try {
            for (Map.Entry<String, List<String>> entry : heads.entrySet()) {
                String key = entry.getKey();
                if (key != null
                        && (key.equalsIgnoreCase("Set-cookie") || key.equalsIgnoreCase("Set-cookie2"))) {
                    for (String cookieStr : entry.getValue()) {
                        try {
                            List<HttpCookie> cs = HttpCookie.parse(cookieStr);
                            for (HttpCookie cookie : cs) {
                                Log.i("Retrofit Network", "store cookie:" + cookie.toString());
                                CookieManager.getInstance().setCookie(url, cookie.toString());
                            }
                            // HmsCookie.put(url, cs);
                        } catch (Exception e) {
                            Log.e("Retrofit Network", "store cookies error", e);
                        }
                    }
                    try {
                        android.webkit.CookieSyncManager.getInstance().sync();
                    } catch (Exception ignore) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
