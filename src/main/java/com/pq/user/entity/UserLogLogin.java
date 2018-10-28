package com.pq.user.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author liutao
 */
public class UserLogLogin implements Serializable {
    private static final long serialVersionUID = 6282811239080280930L;
    private Long id;

    private String userId;

    private String userAgent;

    private String sessionId;

    private String loginIp;

    private Timestamp loginTime;

    private Boolean isPhone;

    private String deviceId;

    private String gtClientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public Boolean getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(Boolean isPhone) {
        this.isPhone = isPhone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getGtClientId() {
        return gtClientId;
    }

    public void setGtClientId(String gtClientId) {
        this.gtClientId = gtClientId == null ? null : gtClientId.trim();
    }
}