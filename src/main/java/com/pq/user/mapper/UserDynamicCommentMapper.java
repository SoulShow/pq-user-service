package com.pq.user.mapper;

import com.pq.user.entity.UserDynamicComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDynamicCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDynamicComment record);

    UserDynamicComment selectByPrimaryKey(Long id);

    List<UserDynamicComment> selectAll();

    int updateByPrimaryKey(UserDynamicComment record);

    List<UserDynamicComment> selectByDynamicId(@Param("dynamicId")Long dynamicId);

    List<UserDynamicComment> selectByStudentId(@Param("studentId")Long studentId,
                                                  @Param("offset") int offset,
                                                  @Param("size") int size);

    List<UserDynamicComment> selectByUserId(@Param("classId")Long classId,
                                            @Param("userId")String userId,
                                            @Param("offset") int offset,
                                            @Param("size") int size);

    UserDynamicComment selectByDynamicIdAndTypeAndUserId(@Param("dynamicId")Long dynamicId,
                                                         @Param("type") int type,
                                                         @Param("userId")String userId);

}