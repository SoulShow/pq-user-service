package com.pq.user.mapper;

import com.pq.user.entity.UserFeedbackImg;

import java.util.List;

public interface UserFeedbackImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFeedbackImg record);

    UserFeedbackImg selectByPrimaryKey(Long id);

    List<UserFeedbackImg> selectAll();

    int updateByPrimaryKey(UserFeedbackImg record);
}