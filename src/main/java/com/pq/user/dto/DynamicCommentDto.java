package com.pq.user.dto;

public class DynamicCommentDto {
    private Long id;

    private Long dynamicId;

    private String originatorUserId;

    private String originatorName;

    private String originatorStudentId;

    private String receiverUserId;

    private String receiverName;

    private String receiverStudentId;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getOriginatorUserId() {
        return originatorUserId;
    }

    public void setOriginatorUserId(String originatorUserId) {
        this.originatorUserId = originatorUserId == null ? null : originatorUserId.trim();
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName == null ? null : originatorName.trim();
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId == null ? null : receiverUserId.trim();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getOriginatorStudentId() {
        return originatorStudentId;
    }

    public void setOriginatorStudentId(String originatorStudentId) {
        this.originatorStudentId = originatorStudentId;
    }

    public String getReceiverStudentId() {
        return receiverStudentId;
    }

    public void setReceiverStudentId(String receiverStudentId) {
        this.receiverStudentId = receiverStudentId;
    }
}