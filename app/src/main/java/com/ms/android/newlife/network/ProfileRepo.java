package com.ms.android.newlife.network;


import com.ms.android.base.network.ApiService;
import com.ms.android.base.network.RxHelper;
import com.ms.android.base.network.entity.TokenEntity;

import rx.Observable;

/**
 * Created by shenjb on 2017/8/24.
 * 个人数据中心处理
 */

public class ProfileRepo  {

    private static ProfileRepo instance;

    private ProfileRepo() {}

    public static final ProfileRepo getInstance() {
        if (instance == null) instance = new ProfileRepo();
        return instance;
    }

    public ProfileAPI getProfileAPI(){
    return  ApiService.getInstance().getProfileAPI(ProfileAPI.class);
   }

    /**
     * 获取七牛Token
     */
    public Observable<TokenEntity> getToken() {
        return getProfileAPI()
                .getQiniuToken()
                .compose(RxHelper.<TokenEntity>handleResult());
    }


}
