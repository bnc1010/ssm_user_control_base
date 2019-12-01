package com.acm.authorization.model;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * @author leeyom
 * @date 2017年10月19日 10:41
 */
public class TokenModel {

    /**
     * 用户id
     */
    private long userId;

    /**
     * 随机生成的uuid
     */
    private String uuid;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 权限码
     */
    private String authorityCode;

    public TokenModel(long userId, String uuid, String timestamp, String authorityCode) {
        this.userId = userId;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.authorityCode = authorityCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getToken() {
        return userId + "_" + timestamp + "_" + uuid + "_" + authorityCode;
    }
}
