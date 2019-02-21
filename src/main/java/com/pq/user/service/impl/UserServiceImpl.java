package com.pq.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.pq.common.constants.CacheKeyConstants;
import com.pq.common.constants.CommonConstants;
import com.pq.common.constants.ParentRelationTypeEnum;
import com.pq.common.exception.CommonErrors;
import com.pq.common.util.*;
import com.pq.user.auth.AuthCookies;
import com.pq.user.dto.AgencyUserDto;
import com.pq.user.dto.RegisterRequestDto;
import com.pq.user.dto.UserDto;
import com.pq.user.entity.User;
import com.pq.user.entity.UserFeedBack;
import com.pq.user.entity.UserLogLogin;
import com.pq.user.entity.UserLogModify;
import com.pq.user.exception.UserErrorCode;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.feign.AgencyFeign;
import com.pq.user.form.AuroraPushIdForm;
import com.pq.user.mapper.UserFeedBackMapper;
import com.pq.user.mapper.UserLogLoginMapper;
import com.pq.user.mapper.UserLogModifyMapper;
import com.pq.user.mapper.UserMapper;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.service.SessionService;
import com.pq.user.service.UserService;
import com.pq.user.utils.ConstantsUser;
import com.pq.user.utils.UserResult;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private UserLogModifyMapper userLogModifyMapper;

    @Autowired
    private UserLogLoginMapper userLogLoginMapper;

    @Autowired
    private AgencyFeign agencyFeign;
    @Autowired
    private UserFeedBackMapper userFeedBackMapper;

    @Value("${php.url}")
    private String phpUrl;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto getUserDtoByUserId(String userId) {
        User userEntity = userMapper.selectByUserId(userId);
        if (userEntity == null) {
            UserException.raise(UserErrors.USER_NOT_FOUND);
        }
        return  transformUserEntityToUserDto(userEntity);
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
        UserResult<List<AgencyUserDto>> result = agencyFeign.getAgencyUserStudent(user.getUserId());
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new UserException(new UserErrorCode(result.getStatus(),result.getMessage()));
        }
        List<AgencyUserDto> list = result.getData();
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setPhone(user.getPhone());
        userDto.setHuanxinId(user.getHuanxinId());
        userDto.setPicture(user.getAvatar());
        userDto.setRole(user.getRole());
        userDto.setAddress(user.getAddress());
        userDto.setUserId(user.getUserId());
        userDto.setStudentList(list);
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
    public User getUserByPhone(String phone,int role) {
        return userMapper.selectByPhoneAndRole(phone,role);
    }

    @Override
    public String register(RegisterRequestDto registerRequestDto) {
        LOGGER.info("用户注册数据注册开始*********");

        checkRegisterInfo(registerRequestDto);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", registerRequestDto.getPhone()+registerRequestDto.getRole());
        paramMap.put("passWord", registerRequestDto.getPhone()+registerRequestDto.getRole());
        try {
            String result = HttpUtil.sendJson(phpUrl+"addUser",new HashMap<>(),JSON.toJSONString(paramMap));
            UserResult userResult = JSON.parseObject(result,UserResult.class);
            if(userResult==null||!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())){
                UserException.raise(UserErrors.USER_HUANXIN_REGISTER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UserException.raise(UserErrors.USER_HUANXIN_REGISTER_ERROR);
        }
        LOGGER.info("用户注册数据*********");
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
        userEntity.setRole(registerRequestDto.getRole());
        userEntity.setRequestFrom(registerRequestDto.getRequestFrom());
        userEntity.setName(registerRequestDto.getName());
        userEntity.setHuanxinId(registerRequestDto.getPhone()+userEntity.getRole());
        //TODO 测试传USER_REVIEW_STATUS_SUCCESS，需要审核传  USER_REVIEW_STATUS_WAITING
        userEntity.setReviewStatus(ConstantsUser.USER_REVIEW_STATUS_SUCCESS);
        if(userEntity.getRole()==CommonConstants.PQ_LOGIN_ROLE_TEACHER){
            userEntity.setReviewStatus(ConstantsUser.USER_REVIEW_STATUS_WAITING);
        }
        try {
            userMapper.insert(userEntity);
            LOGGER.info("用户数据插入库************");

        } catch (Exception e) {
            e.printStackTrace();
            UserException.raise(UserErrors.REGISTER_ERROR);
        }
        return userEntity.getUserId();
    }

    private void checkRegisterInfo(RegisterRequestDto registerRequestDto) {
        if (registerRequestDto.getAgree() == null || !registerRequestDto.getAgree()) {
            UserException.raise(UserErrors.REGISTER_AGREE_USER_AGREEMENT_ERROR);
        }
        if (registerRequestDto.getPhone() == null || !OtherUtil.verifyPhone(registerRequestDto.getPhone())) {
            UserException.raise(UserErrors.REGISTER_ERROR_MOBILE);
        }
        User user = getUserByPhone(registerRequestDto.getPhone(),registerRequestDto.getRole());
        if (user != null) {
            //待审核
            if(user.getReviewStatus()==0){
                UserException.raise(UserErrors.USER_WAIT_REVIEW);
            }
            //已通过
            if(user.getReviewStatus()==1){
                UserException.raise(UserErrors.USER_PHONE_IS_EXITS);
            }
        }
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
    public UserLogLogin getLastUserLoginLog(String userId) {
        return userLogLoginMapper.selectLastUserLoginLog(userId);
    }

    @Override
    public void setPassword(String userId, String phone, String oldPassword, String repPassword,int role) {

        User userEntity = userMapper.selectByPhoneAndRole(phone,role);

        if (userEntity == null) {
            UserException.raise(UserErrors.USER_NOT_FOUND);
        }
        if (!userEntity.getUserId().equals(userId)) {
            UserException.raise(UserErrors.USER_INFO_NOT_MATCH_ERROR);
        }
        if(!passwordUtil.checkPassword(oldPassword, userEntity.getPassword())){
            UserException.raise(UserErrors.USER_OLD_PASSWORD_ERROR);
        }
        User originUserEntity = new User();
        BeanUtils.copyProperties(userEntity, originUserEntity);

        userEntity.setPassword(encodePassword(repPassword));
        userEntity.setUpdatedTime(DateUtil.currentTime());

        updateUser(userEntity, originUserEntity, ConstantsUser.USER_MODIFY_TYPE_SET_PASSWORD);
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }
    @Override
    public void feedback(String userId ,String content){
        UserFeedBack userFeedBack = new UserFeedBack();
        userFeedBack.setUserId(userId);
        userFeedBack.setContent(content);
        userFeedBack.setStatus(0);
        userFeedBack.setCreatedTime(DateUtil.currentTime());
        userFeedBack.setUpdatedTime(DateUtil.currentTime());
        userFeedBackMapper.insert(userFeedBack);
    }

    @Override
    public void updateAuroraPushId(AuroraPushIdForm auroraPushIdForm){
        LOGGER.info("更新极光id********userId"+auroraPushIdForm.getUserId());
        LOGGER.info("更新极光id********极光id"+auroraPushIdForm.getAuroraPushId());

        User user = userMapper.selectByUserId(auroraPushIdForm.getUserId());
        if(user==null){
            return;
        }
        LOGGER.info("***************"+auroraPushIdForm.getUserId());
        LOGGER.info("************极光id"+auroraPushIdForm.getAuroraPushId());

        user.setAuroraPushId(auroraPushIdForm.getAuroraPushId());
        user.setUpdatedTime(DateUtil.currentTime());
        userMapper.updateByPrimaryKey(user);
    }




}
