package com.ms.android.update.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 获取app升级的接口
 *
 * @author zhangky@chinasunfun.com
 * @see <a href="http://manage.mtop.daily.huha.me/work/project/api/585/edit?projectId=18"/>
 * @since 2017/2/18
 */
public class GetVersionDTO implements Parcelable {
    private String appId;//应用的标识
    private String appName;//分配的应用名称
    private String appVersion;//应用当前的版本号格式：1.0.1
    private String force;//是否强制升级
    private String url;//升级的url
    private String describe;//简要的描述

    public GetVersionDTO() {
    }

    protected GetVersionDTO(Parcel in) {
        appId = in.readString();
        appName = in.readString();
        appVersion = in.readString();
        force = in.readString();
        url = in.readString();
        describe = in.readString();
    }

    public static final Creator<GetVersionDTO> CREATOR = new Creator<GetVersionDTO>() {
        @Override
        public GetVersionDTO createFromParcel(Parcel in) {
            return new GetVersionDTO(in);
        }

        @Override
        public GetVersionDTO[] newArray(int size) {
            return new GetVersionDTO[size];
        }
    };

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public boolean getForce() {
        return "true".equals(force);
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appId);
        dest.writeString(appName);
        dest.writeString(appVersion);
        dest.writeString(force);
        dest.writeString(url);
        dest.writeString(describe);
    }
}
