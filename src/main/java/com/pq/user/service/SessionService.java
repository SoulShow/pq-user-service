package com.pq.user.service;


import java.util.Set;

/**
 * session相关操作
 */
public interface SessionService {

    /**
     * 添加用户session
     *
     * @param userId
     * @param sessionId
     */
    void addUserSession(String userId, String sessionId);

    /**
     * 获取某用户所有的session
     *
     * @param userId
     * @param sessionId
     * @return
     */
    void deleteUserSession(String userId, String sessionId);


    /**
     * 删除某用户的所有session
     * @param userId
     */
    void deleteUserSession(String userId);

    /**
     * 获取用户所以seesionId
     * @param userId
     * @return
     */
     Set<String> getSessionIdsByUserId(String userId);

}
