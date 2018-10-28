package com.pq.user.service.impl;

import cn.jpush.api.utils.StringUtils;
import com.pq.common.captcha.UserCaptchaType;
import com.pq.common.constants.CacheKeyConstants;
import com.pq.common.util.DateUtil;
import com.pq.common.util.Password;
import com.pq.common.util.UserIdGenerator;
import com.pq.user.dto.UserDto;
import com.pq.user.entity.User;
import com.pq.user.entity.UserLogLogin;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.mapper.UserLogLoginMapper;
import com.pq.user.mapper.UserMapper;
import com.pq.user.service.LoginService;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.service.SessionService;
import com.pq.user.service.UserService;
import com.pq.user.utils.ConstantsUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author liutao
 */
@Service
public class LoginServiceImpl implements LoginService {
    public static final String USER_AGENT = "XUser-Agent";

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
    private MobileCaptchaService captchaService;
    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto authenticationByCode(String username, String code, HttpServletRequest request, int requestFrom) throws Exception {
        captchaService.verify(username, UserCaptchaType.LOGIN.getCode(), code);

        User user = userMapper.selectByPhone(username);
        if (user != null) {
            //手机登陆已注册用户
            if (user.getStatus() == ConstantsUser.USER_STATUS_LOCKED) {
                UserException.raise(UserErrors.USER_IS_LOCKED);
            }
        } else {
            //手机登陆没有注册
            user = new User();
            user.setUsername(username);
            user.setPhone(username);
            user.setStatus(ConstantsUser.USER_STATUS_NORMAL);
            user.setRequestFrom(requestFrom);
            user.setRegisterTime(DateUtil.currentTime());
            user.setCreatedTime(DateUtil.currentTime());
            user.setUpdatedTime(DateUtil.currentTime());
            user.setUserId(UserIdGenerator.generator());
            userMapper.insert(user);
        }

        UserDto userDto = userService.transformUserEntityToUserDto(user);
        // 记录session，表示登录状态
        HttpSession session = request.getSession();
        session.setAttribute(ConstantsUser.SESSION_USER_ID_KEY, user.getUserId());

        // 记录用户id与session id的映射
        sessionService.addUserSession(user.getUserId(), session.getId());
        UserLogLogin userLogLogin = new UserLogLogin();
        userLogLogin.setUserId(user.getUserId());
        userLogLogin.setUserAgent(request.getHeader(USER_AGENT));
        userLogLogin.setSessionId(session.getId());
        userLogLogin.setLoginTime(DateUtil.currentTime());
        userLogLogin.setLoginIp(getRequestIP(request));

        Object deviceId = request.getHeader("XDevice");
        if (deviceId == null) {
            userLogLogin.setIsPhone(false);
        } else {
            userLogLogin.setIsPhone(true);
            userLogLogin.setDeviceId((String) deviceId);
        }
        Object gtClientId = request.getHeader("GTClientId");
        if (gtClientId != null) {
            userLogLogin.setGtClientId((String) gtClientId);
        }

        userLogLoginMapper.insert(userLogLogin);
        return userDto;
    }

    @Override
    public UserDto authentication(String username, String passwordPlain, HttpServletRequest request) throws Exception {
        UserDto userDto = authentication(username, passwordPlain);
        // 记录session，表示登录状态
        HttpSession session = request.getSession();
        session.setAttribute(ConstantsUser.SESSION_USER_ID_KEY, userDto.getUserId());

        // 记录用户id与session id的映射
        sessionService.addUserSession(userDto.getUserId(), session.getId());
        UserLogLogin userLogLogin = new UserLogLogin();
        userLogLogin.setUserId(userDto.getUserId());
        userLogLogin.setUserAgent(request.getHeader(USER_AGENT));
        userLogLogin.setSessionId(session.getId());
        userLogLogin.setLoginTime(DateUtil.currentTime());
        userLogLogin.setLoginIp(getRequestIP(request));

        Object deviceId = request.getHeader("XDevice");
        if (deviceId == null) {
            userLogLogin.setIsPhone(false);
        } else {
            userLogLogin.setIsPhone(true);
            userLogLogin.setDeviceId((String) deviceId);
        }
        Object gtClientId = request.getHeader("GTClientId");
        if (gtClientId != null) {
            userLogLogin.setGtClientId((String) gtClientId);
        }
        userLogLoginMapper.insert(userLogLogin);
        return userDto;
    }


    private UserDto authentication(String username, String passwordPlain) throws Exception {
        User userEntity = userMapper.selectByPhone(username);

        if (userEntity == null) {
            UserException.raise(UserErrors.USER_NOT_FOUND);
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
    public String authentication(String username, String passwordPlain, String next,
                                 HttpServletRequest request) throws Exception {
        authentication(username, passwordPlain, request);
        // 登录成功后跳转
        String redirectURL;
        if (StringUtils.isEmpty(next)) {
            // 获取域名
            StringBuffer accessUrl = request.getRequestURL();

            String tempContextUrl = accessUrl
                    .delete(accessUrl.length() - request.getRequestURI().length(), accessUrl.length())
                    .append(request.getServletContext().getContextPath()).toString();

            redirectURL = tempContextUrl;
        } else {
            redirectURL = URLDecoder.decode(next, "utf-8");
        }

        return redirectURL;
    }

    @Override
    public void logout(String userId, String sessionId) {
        sessionService.deleteUserSession(userId, sessionId);
    }

    @Override
    public Integer loginTryTimes(String userName) {
        Integer tryTimes = 0;
        User userEntity = userMapper.selectByPhone(userName);
        if (userEntity != null) {
            tryTimes = getUserPasswordErrorLogNumber(userEntity.getUserId());
        }

        return tryTimes;
    }

    @Override
    public Integer loginTryTimesRemain(String username) {
        return passwordErrorLogMaxNumber - loginTryTimes(username);
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


    /**
     * 获取请求对象的IP
     *
     * @param request
     * @return
     */
    private String getRequestIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
