package com.pq.user.mapper;

import com.pq.user.entity.UserThirdParty;
import java.util.List;

public interface UserThirdPartyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserThirdParty record);

    UserThirdParty selectByPrimaryKey(Long id);

    List<UserThirdParty> selectAll();

    int updateByPrimaryKey(UserThirdParty record);
}