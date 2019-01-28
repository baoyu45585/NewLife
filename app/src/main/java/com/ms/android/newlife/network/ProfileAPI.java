package com.ms.android.newlife.network;


import com.ms.android.base.network.BaseType;
import com.ms.android.base.network.entity.TokenEntity;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by shenjb on 2017/8/24.
 * 个人中心数据服务
 */
public interface ProfileAPI {



    /**
     *获取七牛Token
     * see <a href = "https://www.showdoc.cc/1685819?page_id=63041106984720"/>
     * @return
     */
    @GET("/api/bp/getQiniuToken")
    Observable<BaseType<TokenEntity>> getQiniuToken();




}
