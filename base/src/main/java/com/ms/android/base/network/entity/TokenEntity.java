package com.ms.android.base.network.entity;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class TokenEntity {


    /**
     * token : gmgMXoXcXKp3goNGYCM5KKnJxBvebTKv6SD_gQn3:iS22DgxCxejW3_sh9n5kSJ2ifAM=:eyJzY29wZSI6ImxvdWJhYXBwIiwiZGVhZGxpbmUiOjE1MTgzMzg5MzcsImluc2VydE9ubHkiOjAsImRldGVjdE1pbWUiOjAsImZzaXplTGltaXQiOjB9
     * expirationTime : 1518338937
     */

    private String token;
    private long expirationTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
