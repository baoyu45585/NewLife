package com.ms.android.base.network;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class OkHttpCallback<T> extends Subscriber<T> {

    public OkHttpCallback() {
    }

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ServerException) {//跳转设置参数页面
            unbound();
//            if (ActivityManager.getInstance().currentActivity()==null||!ActivityManager.getInstance().currentActivity().getClass().equals(SplashActivity.class)){
//                EventBus.getDefault().post(new BindEvent(true));
//            }
            return;
        } else if (e instanceof ServerWrongException) {//403错误，错误不做任何处理

        } else if (e instanceof ServerStatusException) {
            error(e.getMessage());
        } else if (e instanceof SocketTimeoutException) {
            error("网络连接超时~");
        } else if (e instanceof ConnectException) {
            error("连接的网络不可用~");
        }else if (e instanceof JsonSyntaxException){
            error("解析失败~");
        }else if (e instanceof HttpException){
            error("服务器错误~");
        }else{
            e.printStackTrace();
        }
        _onComplete(false);
    }

    @Override
    public void onCompleted() {
        _onComplete(true);
    }

   //201设备还未绑定
    protected  void unbound(){}

    protected abstract void success(T t);

    protected abstract void error(String message);

    protected abstract void _onComplete(boolean isSuccess);

    protected String getCustomExceptionMessage() {
        return "请求失败，请稍后再试...";
    }
}