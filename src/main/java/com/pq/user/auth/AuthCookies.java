package com.pq.user.auth;

import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

/**
 * 授权信息Cookies
 *
 * @author liutao
 */
public class AuthCookies {
    private String clientUnique;
    private String userId;
    private String userName;
    private String token;
    private String randNumber;

    public void setClientUnique(String unique) {
        this.clientUnique = unique;
    }

    public String getClientUnique() {
        return clientUnique;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRandNumber(String randNumber) {
        this.randNumber = randNumber;
    }

    public String getRandNumber() {
        return randNumber;
    }

    public boolean hasEmptyParam() {
        if (StringUtils.isEmpty(this.getClientUnique())
                || StringUtils.isEmpty(this.getUserId())
                || StringUtils.isEmpty(this.getRandNumber())) {
            return true;
        }
        return false;
    }

    public String getCryptStr() {
        String formatStr = format("%s,%s,%s", getUserId(),
                getClientUnique(), getRandNumber());
        return formatStr;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AuthCookies{");
        sb.append("clientUnique='").append(clientUnique).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", randNumber='").append(randNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
