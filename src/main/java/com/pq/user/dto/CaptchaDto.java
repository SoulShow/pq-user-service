package com.pq.user.dto;


import java.sql.Timestamp;

public class CaptchaDto {
    private String code;

    public CaptchaDto() {}

    public CaptchaDto(Timestamp expired, int coolDown,String code) {
        super();
        this.expired = expired;
        this.coolDown = coolDown;
        this.code = code;
    }

    private Timestamp expired;

    private int  coolDown;

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public Timestamp getExpired() {
        return expired;
    }

    public void setExpired(Timestamp expired) {
        this.expired = expired;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
