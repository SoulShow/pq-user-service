package com.pq.user.dto;


/**
 * @author liutao
 */
public class RegisterRequestDto {
    private String phone;
    private String verifyCode;
    private String password;
    private Boolean agree;
    private String wxCode;
    private int requestFrom;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public int getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(int requestFrom) {
        this.requestFrom = requestFrom;
    }
}
