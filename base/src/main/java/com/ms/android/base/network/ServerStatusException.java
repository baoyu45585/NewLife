package com.ms.android.base.network;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class ServerStatusException extends Exception{

    public ServerStatusException()  {}                //用来创建无参数对象
    public ServerStatusException(String message) {        //用来创建指定参数对象
        super(message);                             //调用超类构造器
    }
}
