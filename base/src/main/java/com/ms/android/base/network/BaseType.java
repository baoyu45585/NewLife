package com.ms.android.base.network;


/**
 * Created by shenjb on 2017/10/12.
 * 服务端返回基本数据
 */

public class BaseType<T> {


    /**
     * code :201设备还未绑定，200成功
     * data :内容
     * message : 说明
     */

    private int code;
    private T data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseType{" +
                "code='" + code + '\'' +
                ", contentData=" + data +
                ", message=" + message +
                '}';
    }
}
