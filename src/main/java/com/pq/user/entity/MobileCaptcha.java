package com.pq.user.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author liutao
 */
public class MobileCaptcha implements Serializable {
    private static final long serialVersionUID = -7759256760014929223L;
    private Long id;

    private String mobile;

    private String code;

    private Long typeId;

    private Timestamp createdTime;

    private Timestamp expiredTime;

    private Timestamp resentTime;

    private Timestamp usedTime;

    private Integer failures;

    private Timestamp updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Timestamp getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Timestamp expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Timestamp getResentTime() {
        return resentTime;
    }

    public void setResentTime(Timestamp resentTime) {
        this.resentTime = resentTime;
    }

    public Timestamp getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Timestamp usedTime) {
        this.usedTime = usedTime;
    }
}