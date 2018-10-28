package com.pq.user.service.impl;

import com.google.gson.Gson;
import com.pq.common.captcha.UserCaptchaType;
import com.pq.common.constants.CacheKeyConstants;
import com.pq.common.constants.CommonConstants;
import com.pq.common.util.*;
import com.pq.user.auth.AuthCookies;
import com.pq.user.dto.MiniProgramDto;
import com.pq.user.dto.RegisterRequestDto;
import com.pq.user.dto.UserDto;
import com.pq.user.entity.User;
import com.pq.user.entity.UserLogLogin;
import com.pq.user.entity.UserLogModify;
import com.pq.user.entity.UserThirdParty;
import com.pq.user.exception.UserErrorCode;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.mapper.UserLogLoginMapper;
import com.pq.user.mapper.UserLogModifyMapper;
import com.pq.user.mapper.UserMapper;
import com.pq.user.mapper.UserThirdPartyMapper;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.service.UserService;
import com.pq.user.utils.ConstantsUser;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * @author liutao
 */
@Service
public class UserServiceImpl implements UserService {
    @Value("${user.secret_key}")
    private String authSecretKey;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Password passwordUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MobileCaptchaService mobileCaptchaService;

    @Autowired
    private UserLogModifyMapper userLogModifyMapper;


    @Autowired
    private UserThirdPartyMapper thirdPartyMapper;
    @Autowired
    private UserLogLoginMapper userLogLoginMapper;

    @Value("${mini.appid}")
    private String miniAppId;

    @Value("${mini.secret_key}")
    private String miniSecretKey;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto getUserDtoByUserId(String userId) {
        String userDtoCacheKey = CacheKeyConstants.USER_DTO_PREFIX + userId;
        Long userDtoCacheExpire = CacheKeyConstants.USER_DTO_EXPIRE;

        UserDto userDto;

        if (redisTemplate.hasKey(userDtoCacheKey)) {
            userDto = (UserDto) redisTemplate.opsForValue().get(userDtoCacheKey);
        } else {
            User userEntity = userMapper.selectByUserId(userId);
            if (userEntity == null) {
                UserException.raise(UserErrors.USER_NOT_FOUND);
            }
            userDto = transformUserEntityToUserDto(userEntity);

            redisTemplate.opsForValue().set(userDtoCacheKey, userDto, userDtoCacheExpire, TimeUnit.SECONDS);
        }

        return userDto;
    }

    @Override
    public void clearUserDtoCacheByUserId(String userId) {
        String userDtoCacheKey = CacheKeyConstants.USER_DTO_PREFIX + userId;
        redisTemplate.delete(userDtoCacheKey);
    }

    @Override
    public void clearUserPasswordErrorLog(String userId) {
        String redisKeyPattern = CacheKeyConstants.USER_PASSWORD_ERROR_LOG_KEY_PREFIX + userId;
        Set<String> stringSet = (redisTemplate.keys(redisKeyPattern + "_*"));

        redisTemplate.delete(stringSet);
    }

    @Override
    public UserDto transformUserEntityToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setGender(user.getGender());
        userDto.setPhone(user.getPhone());
        userDto.setName(user.getName());
        userDto.setNickname(user.getNickname());
        userDto.setAvatar(user.getAvatar());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    @Override
    public String encodePassword(String passwordPlain) {
        return passwordUtil.encode(passwordPlain);
    }


    @Override
    public boolean validate(AuthCookies authCookies) {
        if (authCookies == null || authCookies.hasEmptyParam() || StringUtils.isEmpty(authCookies.getToken())) {
            LOGGER.warn(format("Token 验证无效，Cookie：%s", authCookies));
            UserException.raise(UserErrors.COOKIE_NOT_VALID);
        }
        if (StringUtils.isBlank(authSecretKey)) {
            LOGGER.error("Auth Secret Key 未配置！");
            UserException.raise(UserErrors.AUTH_SECRET_KEY_NOT_FOUND);
        }
        String cryptHash = getCryptHash(authCookies.getCryptStr());
        String token = authCookies.getToken();
        if (!token.equals(cryptHash)) {
            LOGGER.warn(format("登录验证失败:Cookie：%s, Token：%s", authCookies.getCryptStr(), cryptHash));
        }

        return token.equals(cryptHash);
    }


    private String getCryptHash(String str) {
        String sryptHash = Crypt.crypt(str, authSecretKey);

        sryptHash = sryptHash.substring(sryptHash.lastIndexOf("$") + 1);
        return sryptHash;
    }

    @Override
    public User getUserByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public String register(RegisterRequestDto registerRequestDto) {
        checkRegisterInfo(registerRequestDto);

        User userEntity = new User();
        Timestamp currentTime = DateUtil.currentTime();

        userEntity.setUserId(UserIdGenerator.generator());
        userEntity.setUsername(registerRequestDto.getPhone());
        userEntity.setPassword(encodePassword(registerRequestDto.getPassword()));
        userEntity.setPhone(registerRequestDto.getPhone());
        userEntity.setStatus(ConstantsUser.USER_STATUS_NORMAL);
        userEntity.setRegisterTime(currentTime);
        userEntity.setCreatedTime(currentTime);
        userEntity.setUpdatedTime(currentTime);
        userEntity.setRequestFrom(registerRequestDto.getRequestFrom());
        try {
            userMapper.insert(userEntity);
            if (StringUtils.isNotEmpty(registerRequestDto.getWxCode())) {
                saveThirdPartUser(registerRequestDto.getWxCode(), userEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UserException.raise(UserErrors.REGISTER_ERROR);
        }
        return userEntity.getUserId();
    }

    private void saveThirdPartUser(String wxCode, User user) {
        //获取微信小程序登陆信息
        MiniProgramDto miniProgramDto = getMiniProgramInfoSessionInfo(wxCode);
        UserThirdParty userThirdParty = new UserThirdParty();
        userThirdParty.setUserId(user.getUserId());
        userThirdParty.setOpenId(miniProgramDto.getOpenid());
        userThirdParty.setUnionId(miniProgramDto.getUnionid());
        userThirdParty.setThirdPartType(CommonConstants.THIRD_PART_TYPE_WXCHAT);
        userThirdParty.setCreatedTime(DateUtil.currentTime());
        userThirdParty.setUpdatedTime(DateUtil.currentTime());
        thirdPartyMapper.insert(userThirdParty);
    }

    private void checkRegisterInfo(RegisterRequestDto registerRequestDto) {
        if (registerRequestDto.getAgree() == null || !registerRequestDto.getAgree()) {
            UserException.raise(UserErrors.REGISTER_AGREE_USER_AGREEMENT_ERROR);
        }
        if (registerRequestDto.getPhone() == null || !OtherUtil.verifyPhone(registerRequestDto.getPhone())) {
            UserException.raise(UserErrors.REGISTER_ERROR_MOBILE);
        }
        User user = getUserByPhone(registerRequestDto.getPhone());
        if (user != null) {
            UserException.raise(UserErrors.USER_PHONE_IS_EXITS);
        }
        mobileCaptchaService.verify(registerRequestDto.getPhone(), UserCaptchaType.REGISTER.getCode(),
                registerRequestDto.getVerifyCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User userEntity, User originUserEntity, Integer modifyType) {
        try {
            userEntity.setUpdatedTime(DateUtil.currentTime());
            userMapper.updateByPrimaryKey(userEntity);

            UserLogModify userLogModify = new UserLogModify();
            userLogModify.setUserId(userEntity.getUserId());
            userLogModify.setOrigin(JsonUtil.toJson(originUserEntity));
            userLogModify.setTarget(JsonUtil.toJson(userEntity));
            userLogModify.setType(modifyType);
            userLogModify.setCreatedTime(DateUtil.currentTime());
            userLogModifyMapper.insert(userLogModify);
            // 清空缓存
            clearUserDtoCacheByUserId(userEntity.getUserId());
        } catch (Exception e) {
            UserException.raise(UserErrors.USER_UPDATE_FAILED, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        User originUserEntity = userMapper.selectByUserId(user.getUserId());
        updateUser(user, originUserEntity, ConstantsUser.USER_MODIFY_TYPE_USERINFO);
    }


    @Override
    public User getUserByUserId(String userId) {
        return userMapper.selectByUserId(userId);
    }

    @Override
    public MiniProgramDto getMiniProgramInfoSessionInfo(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = null;
        httpGet = new HttpGet(url + "appid=" + miniAppId + "&secret=" + miniSecretKey
                + "&js_code=" + code + "&grant_type=authorization_code");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            String response = EntityUtils.toString(httpResponse.getEntity(), Charset.forName("utf-8"));
            Gson gson = new Gson();
            Map<String, String> resMap = gson.fromJson(response, Map.class);
            if (resMap.get("errcode") != null) {
                UserException.raise(new UserErrorCode(String.valueOf(resMap.get("errcode")), resMap.get("errmsg")));
            }
            MiniProgramDto miniProgramDto = gson.fromJson(response, MiniProgramDto.class);
            return miniProgramDto;

        } catch (IOException e) {
            UserException.raise(UserErrors.PAY_MINI_PROGRAM_NOT_EXIST_ERROR);
        }
        return null;
    }


    @Override
    public UserLogLogin getLastUserLoginLog(String userId) {
        return userLogLoginMapper.selectLastUserLoginLog(userId);
    }

    @Override
    public void setPassword(String userId, String phone, String password, String repPassword) {
        User userEntity = userMapper.selectByPhone(phone);

        if (userEntity == null) {
            UserException.raise(UserErrors.USER_NOT_FOUND);
        }
        if (!userEntity.getUserId().equals(userId)) {
            UserException.raise(UserErrors.USER_INFO_NOT_MATCH_ERROR);
        }
        if (!StringUtils.isEmpty(userEntity.getPassword())) {
            UserException.raise(UserErrors.USER_PASSWORD_IS_EXIST_ERROR);
        }
        User originUserEntity = new User();
        BeanUtils.copyProperties(userEntity, originUserEntity);

        userEntity.setPassword(encodePassword(password));
        userEntity.setUpdatedTime(DateUtil.currentTime());

        updateUser(userEntity, originUserEntity, ConstantsUser.USER_MODIFY_TYPE_SET_PASSWORD);
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

}
