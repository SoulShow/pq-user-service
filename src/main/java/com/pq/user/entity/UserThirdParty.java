package com.pq.user.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author liutao
 */
public class UserThirdParty implements Serializable {
    private static final long serialVersionUID = -4139301005919991134L;
    private Long id;

    private String userId;

    private String openId;

    private String unionId;

    private Integer thirdPartType;

    private Timestamp createdTime;

    private Timestamp updatedTime;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId == null ? null : unionId.trim();
    }

    public Integer getThirdPartType() {
        return thirdPartType;
    }

    public void setThirdPartType(Integer thirdPartType) {
        this.thirdPartType = thirdPartType;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}