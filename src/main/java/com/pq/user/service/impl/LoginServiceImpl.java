package com.pq.user.service.impl;

import com.netflix.discovery.CommonConstants;
import com.pq.common.constants.CacheKeyConstants;
import com.pq.common.util.DateUtil;
import com.pq.common.util.Password;
import com.pq.common.util.StringUtil;
import com.pq.user.dto.UserDto;
import com.pq.user.entity.User;
import com.pq.user.entity.UserLogLogin;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.form.AuthForm;
import com.pq.user.mapper.UserLogLoginMapper;
import com.pq.user.mapper.UserMapper;
import com.pq.user.service.LoginService;
import com.pq.user.service.SessionService;
import com.pq.user.service.UserService;
import com.pq.user.utils.ConstantsUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author liutao
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private Password passwordUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.password_error_log.max_number}")
    private Integer passwordErrorLogMaxNumber;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserLogLoginMapper userLogLoginMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto authentication(AuthForm authForm) throws Exception {
        UserDto userDto = authentication(authForm.getAccount(), authForm.getPassword(),authForm.getRole());
        // 记录用户id与session id的映射
        sessionService.addUserSession(userDto.getUserId(), authForm.getSessionId());
        UserLogLogin userLogLogin = new UserLogLogin();
        userLogLogin.setUserId(userDto.getUserId());
        userLogLogin.setUserAgent(authForm.getUserAgent());
        userLogLogin.setSessionId(authForm.getSessionId());
        userLogLogin.setLoginTime(DateUtil.currentTime());
        userLogLogin.setLoginIp(authForm.getLoginIp());
        userLogLogin.setDeviceId(authForm.getDeviceId());
        userLogLogin.setGtClientId(authForm.getGtClientId());
        userLogLogin.setIsPhone(true);
        if(StringUtil.isEmpty(authForm.getDeviceId())){
            userLogLogin.setIsPhone(false);
        }
        userLogLoginMapper.insert(userLogLogin);
        return userDto;
    }


    private UserDto authentication(String username, String passwordPlain,int role) throws Exception {
        User userEntity = userMapper.selectByPhoneAndRole(username,role);

        if (userEntity == null) {
            UserException.raise(UserErrors.USER_NOT_FOUND);
        }
        UserLogLogin logLogin = userLogLoginMapper.selectLastUserLoginLog(userEntity.getUserId());
        if(userEntity.getIsOld()==1&&logLogin==null){
            UserException.raise(UserErrors.USER_OLD_FIRST_LOGIN_TIPS);
        }

        if(role == com.pq.common.constants.CommonConstants.PQ_LOGIN_ROLE_TEACHER){
            if(userEntity.getReviewStatus()==ConstantsUser.USER_REVIEW_STATUS_WAITING){
                UserException.raise(UserErrors.USER_WAIT_REVIEW);
            }
            if(userEntity.getReviewStatus()==ConstantsUser.USER_REVIEW_STATUS_FAIL){
                UserException.raise(UserErrors.USER_STOP_REVIEW);
            }
        }

        if (userEntity.getStatus() == ConstantsUser.USER_STATUS_LOCKED) {
            UserException.raise(UserErrors.USER_IS_LOCKED);
        }
        if (userEntity.getPassword() == null && passwordPlain != null) {
            UserException.raise(UserErrors.USER_LOGIN_PASSWORD_ERROR);
        }
        if (passwordUtil.checkPassword(passwordPlain, userEntity.getPassword())) {
            userService.clearUserPasswordErrorLog(userEntity.getUserId());
            return userService.getUserDtoByUserId(userEntity.getUserId());
        }

        addPasswordErrorLog(userEntity.getUserId());
        UserException.raise(UserErrors.USER_LOGIN_PASSWORD_ERROR);
        return null;
    }

    @Override
    public void logout(String userId, String sessionId) {
        sessionService.deleteUserSession(userId, sessionId);
    }

    @Override
    public Integer loginTryTimes(String userName,int role) {
        Integer tryTimes = 0;
        User userEntity = userMapper.selectByPhoneAndRole(userName,role);
        if (userEntity != null) {
            tryTimes = getUserPasswordErrorLogNumber(userEntity.getUserId());
        }

        return tryTimes;
    }

    @Override
    public Integer loginTryTimesRemain(String username,int role) {
        return passwordErrorLogMaxNumber - loginTryTimes(username,role);
    }

    @Override
    public Integer loginTryTimesRemainByUserId(String userId) {
        return passwordErrorLogMaxNumber - getUserPasswordErrorLogNumber(userId);
    }

    @Override
    public Integer getUserPasswordErrorLogNumber(String userId) {
        String redisKeyPattern = CacheKeyConstants.USER_PASSWORD_ERROR_LOG_KEY_PREFIX + userId;
        Set<String> stringSet = (redisTemplate.keys(redisKeyPattern + "_*"));

        return stringSet.size();
    }

    private Boolean checkUserIsFrozen(String userId) {
        Integer logNumber = getUserPasswordErrorLogNumber(userId);

        if (logNumber >= passwordErrorLogMaxNumber) {
            return true;
        }

        return false;
    }

    @Override
    public void addPasswordErrorLog(String userId) {
        if (!checkUserIsFrozen(userId)) {
            String salt = passwordUtil.generateSalt();
            Long currentTime = System.currentTimeMillis();
            String redisKey = CacheKeyConstants.USER_PASSWORD_ERROR_LOG_KEY_PREFIX + userId + "_" + DigestUtils.md5Hex(salt + "." + currentTime);
            redisTemplate.opsForValue().set(redisKey, true, CacheKeyConstants.USER_PASSWORD_ERROR_LOG_KEY_EXPIRE, TimeUnit.SECONDS);
        }
    }




}
