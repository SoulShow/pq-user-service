package com.pq.user.mapper;

import com.pq.user.entity.CaptchaType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaptchaTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CaptchaType record);

    CaptchaType selectByPrimaryKey(Long id);

    List<CaptchaType> selectAll();

    int updateByPrimaryKey(CaptchaType record);

    CaptchaType selectByTypeCode(@Param("typeCode") String typeCode);

}