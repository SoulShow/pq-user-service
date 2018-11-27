package com.pq.user.dto;

import java.util.List;

public class CommentDto {
    private int commentCount;
    private List<DynamicCommentDto> list;


    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<DynamicCommentDto> getList() {
        return list;
    }

    public void setList(List<DynamicCommentDto> list) {
        this.list = list;
    }
}