package com.pq.user.dto;

/**
 * @author liutao
 */
public class UpdatePasswordParamsDto {
    private String userId;
    private String originPassword;
    private String newPassword;

    public String getOriginPassword () {
        return originPassword;
    }

    public void setOriginPassword (String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword () {
        return newPassword;
    }

    public void setNewPassword (String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
