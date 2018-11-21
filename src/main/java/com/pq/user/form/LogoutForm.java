package com.pq.user.form;

import java.io.Serializable;

/**
 * @author liutao
 */
public class LogoutForm implements Serializable {
    private String userId;
    private String sessionId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
