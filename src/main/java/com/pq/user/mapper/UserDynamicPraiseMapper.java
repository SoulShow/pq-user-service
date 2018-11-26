package com.pq.user.mapper;

import com.pq.user.entity.UserDynamicPraise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDynamicPraiseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDynamicPraise record);

    UserDynamicPraise selectByPrimaryKey(Long id);

    List<UserDynamicPraise> selectAll();

    int updateByPrimaryKey(UserDynamicPraise record);

    List<UserDynamicPraise> selectByDynamicId(@Param("dynamicId")Long dynamicId);
}