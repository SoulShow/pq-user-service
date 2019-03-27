package com.pq.user.dto;

public class CommentMessageDto {
    private Long commentId;

    private String originatorUserId;

    private String originatorName;

    private Long originatorStudentId;

    private String originatorAvatar;

    private String className;

    private String receiverUserId;

    private String receiverName;

    private Long receiverStudentId;

    private String content;

    private String createdTime;

    private int isRead;

    private String img;

    private String name;

    private Long dynamicId;

    private int type;

    private String dynamicIdContent;


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

    public Long getOriginatorStudentId() {
        return originatorStudentId;
    }

    public void setOriginatorStudentId(Long originatorStudentId) {
        this.originatorStudentId = originatorStudentId;
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

    public Long getReceiverStudentId() {
        return receiverStudentId;
    }

    public void setReceiverStudentId(Long receiverStudentId) {
        this.receiverStudentId = receiverStudentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getOriginatorAvatar() {
        return originatorAvatar;
    }

    public void setOriginatorAvatar(String originatorAvatar) {
        this.originatorAvatar = originatorAvatar;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getDynamicIdContent() {
        return dynamicIdContent;
    }

    public void setDynamicIdContent(String dynamicIdContent) {
        this.dynamicIdContent = dynamicIdContent;
    }
}