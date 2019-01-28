package com.ms.android.base.network;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/13.
 * Rx网络请求封装帮助类
 */

public class RxHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseType<T>, T> handleResult() {
        return new Observable.Transformer<BaseType<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseType<T>> tObservable) {
                return tObservable.flatMap(new Func1<BaseType<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseType<T> result) {
//                        Logger.d("result from network : " + result);
                        int retType = result.getCode();
                        if (retType == 200) {
                            return createData(result.getData());
                        } else if (retType==201){
                            String resultRet = result.getMessage();
                            return Observable.error(new ServerException(resultRet));
                        }else if (retType==403){
                            String resultRet = result.getMessage();
                            return Observable.error(new ServerWrongException(resultRet));
                        }else {
                            String resultRet = result.getMessage();
                            return Observable.error(new ServerStatusException(resultRet));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 结果（无处理）
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> handleResults() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *（不是安全的需要处理）
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }



}
