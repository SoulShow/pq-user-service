package com.pq.user.mapper;

import com.pq.user.entity.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectByPhone(@Param("phone") String phone);

    User selectByUserId(@Param("userId") String userId);

}