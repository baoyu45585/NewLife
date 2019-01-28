package com.ms.android.base.network.repo;


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

//
//    /**
//     * 绑定设备
//     */
//    public Observable<BindEntity> bindClient(String mac,int action) {
//        return ApiService.getInstance().
//                getProfileAPI().
//                bindClient(mac,action).
//                compose(RxHelper.<BindEntity>handleResult());
//    }

    /**
     * 获取七牛Token
     */
    public Observable<TokenEntity> getToken() {
        return getProfileAPI()
                .getToken()
                .compose(RxHelper.<TokenEntity>handleResult());
    }

}
