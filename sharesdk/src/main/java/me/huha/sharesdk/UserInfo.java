package me.huha.sharesdk;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private String icon;
    private String username;
    private Gender gender;//性别（0-男 1-女 2-未知）
    private String uid;
    private PlatformType platformType;//平台（0-QQ 1-微信 2-微博）

    protected UserInfo(Parcel in) {
        icon = in.readString();
        username = in.readString();
        uid = in.readString();
        int platformStatus = in.readInt();
        if (platformStatus == PlatformType.WECHAT.status) {
            platformType = PlatformType.WECHAT;
        } else if (platformStatus == PlatformType.SINA_WEIBO.status) {
            platformType = PlatformType.SINA_WEIBO;
        } else if (platformStatus == PlatformType.QQ.status) {
            platformType = PlatformType.QQ;
        }

        int genderStatus = in.readInt();
        if (genderStatus == Gender.FEMALE.status) {
            gender = Gender.FEMALE;
        } else if (genderStatus == Gender.MALE.status) {
            gender = Gender.MALE;
        } else {
            gender = Gender.UN_KNOWN;
        }
    }

    public UserInfo() {
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(username);
        dest.writeString(uid);
        dest.writeInt(platformType.status);
        dest.writeInt(gender.status);
    }

    public static enum Gender {
        MALE(0), FEMALE(1), UN_KNOWN(2);
        int status;

        Gender(int status) {
            this.status = status;
        }
    }

    public static enum PlatformType {
        QQ(0), WECHAT(1), SINA_WEIBO(2);
        int status;

        PlatformType(int status) {
            this.status = status;
        }
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platformType) {
        this.platformType = platformType;
    }
}
