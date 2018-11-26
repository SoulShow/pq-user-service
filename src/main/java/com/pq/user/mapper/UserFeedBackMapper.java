package com.pq.user.mapper;

import com.pq.user.entity.UserFeedBack;

import java.util.List;

public interface UserFeedBackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFeedBack record);

    UserFeedBack selectByPrimaryKey(Long id);

    List<UserFeedBack> selectAll();

    int updateByPrimaryKey(UserFeedBack record);
}