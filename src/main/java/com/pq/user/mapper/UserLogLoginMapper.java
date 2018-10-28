package com.pq.user.mapper;

import com.pq.user.entity.UserLogLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserLogLoginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLogLogin record);

    UserLogLogin selectByPrimaryKey(Long id);

    List<UserLogLogin> selectAll();

    int updateByPrimaryKey(UserLogLogin record);

    UserLogLogin selectLastUserLoginLog(@Param("userId")String userId);

}