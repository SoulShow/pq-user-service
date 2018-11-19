package com.pq.user.mapper;

import com.pq.user.entity.MobileCaptcha;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MobileCaptchaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MobileCaptcha record);

    MobileCaptcha selectByPrimaryKey(Long id);

    List<MobileCaptcha> selectAll();

    int updateByPrimaryKey(MobileCaptcha record);

    List<MobileCaptcha> selectByMobileAndType(@Param("mobile") String mobile, @Param("type") String type);

    List<MobileCaptcha> selectNotUsedByMobileAndType(@Param("mobile") String mobile, @Param("type") int type);

    int incrFailuresById(@Param("id") Long id);
}