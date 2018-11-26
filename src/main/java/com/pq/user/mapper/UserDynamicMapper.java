package com.pq.user.mapper;

import com.pq.user.entity.UserDynamic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDynamicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDynamic record);

    UserDynamic selectByPrimaryKey(Long id);

    List<UserDynamic> selectAll();

    int updateByPrimaryKey(UserDynamic record);

    List<UserDynamic> selectUserClassDynamic(@Param("classIdList")List<Long> classIdList,
                                             @Param("offset")int offset, @Param("page") int page);

    void addPraiseCountById(@Param("id") Long id);

    void subPraiseCountById(@Param("id") Long id);

}