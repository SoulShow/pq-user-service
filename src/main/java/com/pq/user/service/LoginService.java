package com.pq.user.service;


import com.pq.user.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    /**
     * 验证码登陆验证
     *
     * @param username
     * @param code
     * @param request
     * @param requestFrom
     * @return
     * @throws Exception
     */
    UserDto authenticationByCode(String username, String code, HttpServletRequest request, int requestFrom) throws Exception;

    /**
     * 登陆验证
     *
     * @param username
     * @param passwordPlain
     * @param request
     * @return
     * @throws Exception
     */
    UserDto authentication(String username, String passwordPlain, HttpServletRequest request) throws Exception;

    /**
     * 登录验证
     * 登录成功后跳转到next，如果next为空则跳转到首页/
     *
     * @param username
     * @param passwordPlain
     * @param next
     * @param request
     * @throws Exception
     */
    String authentication(String username, String passwordPlain, String next,
                          HttpServletRequest request) throws Exception;

    /**
     * 登出
     *
     * @param UserId
     * @param sessionId
     */
    void logout(String UserId, String sessionId);

    /**
     * 登陆尝试次数
     *
     * @param username
     * @return
     */
    Integer loginTryTimes(String username);

    /**
     * 获取密码错误次数
     * @param userId
     * @return
     */
    Integer getUserPasswordErrorLogNumber(String userId);

    Integer loginTryTimesRemain(String username);

    Integer loginTryTimesRemainByUserId(String userId);

    /**
     * 添加错误日志
     * @param userId
     */
    void addPasswordErrorLog(String userId);
}
