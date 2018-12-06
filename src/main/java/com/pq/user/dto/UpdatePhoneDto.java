package com.pq.user.dto;

import java.io.Serializable;

/**
 * 忘记密码的表单
 *
 * @author liutao
 * @date 15/6/3
 */
public class UpdatePhoneDto implements Serializable {
    private static final long serialVersionUID = -3782083093175277519L;
    private String account;
    private String newPhone;
    private String verCode;
    private String sessionId;
    private int role;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}

