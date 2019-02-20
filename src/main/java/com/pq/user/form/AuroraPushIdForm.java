package com.pq.user.form;

import java.io.Serializable;

/**
 * @author liutao
 */
public class AuroraPushIdForm implements Serializable {
    private String auroraPushId;
    private String userId;

    public String getAuroraPushId() {
        return auroraPushId;
    }

    public void setAuroraPushId(String auroraPushId) {
        this.auroraPushId = auroraPushId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
