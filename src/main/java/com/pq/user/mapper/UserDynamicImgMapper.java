package com.pq.user.mapper;

import com.pq.user.entity.UserDynamicImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDynamicImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDynamicImg record);

    UserDynamicImg selectByPrimaryKey(Long id);

    List<UserDynamicImg> selectAll();

    int updateByPrimaryKey(UserDynamicImg record);

    List<String> selectByDynamicId(@Param("dynamicId")Long dynamicId);

}