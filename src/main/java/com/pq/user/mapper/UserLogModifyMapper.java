package com.pq.user.mapper;

import com.pq.user.entity.UserLogModify;
import java.util.List;

public interface UserLogModifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLogModify record);

    UserLogModify selectByPrimaryKey(Long id);

    List<UserLogModify> selectAll();

    int updateByPrimaryKey(UserLogModify record);
}