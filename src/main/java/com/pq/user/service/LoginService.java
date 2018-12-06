package com.pq.user.service;


import com.pq.user.dto.UserDto;
import com.pq.user.form.AuthForm;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
/**
 * 登录服务
 * @author liutao
 */
public interface LoginService {


    /**
     * 登陆验证
     *
     * @param authForm
     * @return
     * @throws Exception
     */
    UserDto authentication(AuthForm authForm) throws Exception;

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
     * @param role
     * @return
     */
    Integer loginTryTimes(String username,int role);

    /**
     * 获取密码错误次数
     * @param userId
     * @return
     */
    Integer getUserPasswordErrorLogNumber(String userId);

    Integer loginTryTimesRemain(String username,int role);

    Integer loginTryTimesRemainByUserId(String userId);

    /**
     * 添加错误日志
     * @param userId
     */
    void addPasswordErrorLog(String userId);
}
