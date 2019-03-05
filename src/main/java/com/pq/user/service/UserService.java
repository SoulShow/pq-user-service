package com.pq.user.service;


import com.pq.user.auth.AuthCookies;
import com.pq.user.dto.RegisterRequestDto;
import com.pq.user.dto.UserDto;
import com.pq.user.entity.User;
import com.pq.user.entity.UserLogLogin;
import com.pq.user.form.AuroraPushIdForm;
import com.pq.user.form.FeedbackForm;

import java.util.List;

/**
 * 用户服务
 * @author liutao
 */
public interface UserService {

    /**
     * 注册
     *
     * @param registerRequestDto
     * @return
     * @throws Exception
     */
    String register(RegisterRequestDto registerRequestDto);

    /**
     * 获取用户基本信息
     *
     * @param userId
     * @return
     */
    UserDto getUserDtoByUserId(String userId);

    /**
     * 清除缓存中的用户信息
     *
     * @param userId
     * @throws Exception
     */
    void clearUserDtoCacheByUserId(String userId);

    /**
     * 登陆信息验证
     *
     * @param authCookies
     * @return
     */
    boolean validate(AuthCookies authCookies);

    /**
     * 通过手机号查询用户信息
     *
     * @param phone
     * @param role
     * @return
     */
    User getUserByPhone(String phone,int role);

    /**
     * 清除用户输入密码错误log
     *
     * @param userId
     */
    void clearUserPasswordErrorLog(String userId);

    /**
     * 加密
     *
     * @param passwordPlain
     * @return
     */
    String encodePassword(String passwordPlain);


    /**
     * 更新用户信息和log信息
     *
     * @param userEntity
     * @param originUserEntity
     * @param modifyType
     */
    void updateUser(User userEntity, User originUserEntity, Integer modifyType);

    /**
     * 更新用户基本信息
     *
     * @param user
     * @return
     */
    void updateUserInfo(User user);

    /**
     * user 转换为userDto
     *
     * @param user
     * @return
     */
    UserDto transformUserEntityToUserDto(User user);


    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    User getUserByUserId(String userId);


    /**
     * 获取最近的登录记录
     *
     * @param userId
     * @return
     */
    UserLogLogin getLastUserLoginLog(String userId);

    /**
     * 设置用户密码
     *
     * @param userId
     * @param phone
     * @param password
     * @param repPassword
     * @param role
     */
    void setPassword(String userId, String phone, String password, String repPassword,int role);

    /**
     * 创建用户
     *
     * @param user
     */
    void insert(User user);

    /**
     * 用户反馈
     *
     * @param userId
     * @param content
     * @param imgList
     */
    void feedback(String userId ,String content,List<String> imgList);

    /**
     * 更新用户极光id
     * @param auroraPushIdForm
     */
    void updateAuroraPushId(AuroraPushIdForm auroraPushIdForm);

}
