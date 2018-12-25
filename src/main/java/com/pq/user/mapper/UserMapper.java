package com.pq.user.mapper;

import com.pq.user.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectByPhoneAndRole(@Param("phone") String phone,@Param("role")int role);

    User selectByUserId(@Param("userId") String userId);

}