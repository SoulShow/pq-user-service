package com.pq.user.dto;

import java.util.List;

public class UserDynamicDto {
    private Long id;

    private String userId;

    private String name;

    private String content;

    private Integer praiseCount;

    private Integer commentCount;

    private String createdTime;

    private int praiseState;

    private String movieUrl;

    private List<String> imgList;

    private List<DynamicPraiseDto> praiseList;

    private List<DynamicCommentDto> commentList;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public List<DynamicPraiseDto> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<DynamicPraiseDto> praiseList) {
        this.praiseList = praiseList;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<DynamicCommentDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<DynamicCommentDto> commentList) {
        this.commentList = commentList;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public int getPraiseState() {
        return praiseState;
    }

    public void setPraiseState(int praiseState) {
        this.praiseState = praiseState;
    }
}