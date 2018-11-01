package com.pq.user.form;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author liutao
 */
public class AuthForm implements Serializable {
    private static final long serialVersionUID = -4745983525768728758L;
    private String account;
    private String password;
    private String deviceId;
    private String userAgent;
    private String loginIp;
    private String gtClientId;
    private HttpSession httpSession;
    private String sessionId;

    public AuthForm() {
    }

    public AuthForm(String account, String password) {
        super();
        this.account = account;
        this.password = password;
    }

    public AuthForm(String account, String password, String deviceId) {
        super();
        this.account = account;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getGtClientId() {
        return gtClientId;
    }

    public void setGtClientId(String gtClientId) {
        this.gtClientId = gtClientId;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
