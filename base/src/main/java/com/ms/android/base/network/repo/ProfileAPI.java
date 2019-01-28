package com.ms.android.base.network.repo;


import com.ms.android.base.network.BaseType;
import com.ms.android.base.network.entity.TokenEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by shenjb on 2017/8/24.
 * 个人中心数据服务
 */
public interface ProfileAPI {

//    /**
//     * 获取设备配置文件
//     *see <a href = "https://www.showdoc.cc/1671622?page_id=15492834"/>
//     * @return
//     */
//    @GET("/api/bp/getClientConfig")
//    Observable<BaseType<ConfigurationEntity>> getConfiguration();
//
////    /**
////     * 获取天气预报接口
////     *see <a href = "https://www.showdoc.cc/1685819?page_id=15704282"/>
////     * @return
////     */
////    @FormUrlEncoded
////    @POST("/api/bp/getWeather")
////    Observable<BaseType<List<WeatherEntity>>> getWeather(@Field("city") String city, @Field("num")int num);


    /**
     *获取七牛Token
     * see <a href = "https://www.showdoc.cc/1685819?page_id=63041106984720"/>
     * @return
     */
    @GET("/api/bp/getQiniuToken")
    Observable<BaseType<TokenEntity>> getToken();

    /**
     * 下载文件
     * @param fileUrl
     * @return
     */
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
