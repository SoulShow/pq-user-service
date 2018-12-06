package com.pq.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.pq.common.captcha.UserCaptchaType;
import com.pq.common.exception.ErrorCode;
import com.pq.common.util.HttpUtil;
import com.pq.user.dto.CaptchaDto;
import com.pq.user.entity.CaptchaType;
import com.pq.user.entity.MobileCaptcha;
import com.pq.user.entity.User;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.mapper.CaptchaTypeMapper;
import com.pq.user.mapper.MobileCaptchaMapper;
import com.pq.user.mapper.UserMapper;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.utils.ConstantsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * @author liutao
 */
@Service
public class MobileCaptchaServiceImpl implements MobileCaptchaService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MobileCaptchaServiceImpl.class);

    @Value("${captcha.service.isBlockSendSMS}")
    private boolean isBlockSendSMS = true;

    @Value("${captcha.service.maxFailedCount}")
    private int maxFailedCount;

    @Value("${captcha.service.maxSendCountOfDay}")
    private int maxSendCountOfDay;

    @Value("${captcha.service.captchaTimeOut}")
    private long captchaTimeOut;

    @Value("${captcha.service.sendCaptchaIntervalTime}")
    private long sendCaptchaIntervalTime;

    @Value("${captcha.service.coolDown}")
    private int captchaCoolDown;

    @Autowired
    private MobileCaptchaMapper mobileCaptchaMapper;

    @Autowired
    private CaptchaTypeMapper captchaTypeMapper;
    @Autowired
    private UserMapper userMapper;

    @Value("${php.url}")
    private String phpUrl;


    @Override
    public boolean canSend(String mobile, String type) {

        User user = userMapper.selectByPhone(mobile);
        if (user != null && user.getStatus() == ConstantsUser.USER_STATUS_LOCKED) {
            UserException.raise(UserErrors.USER_IS_LOCKED);
        }
        if (UserCaptchaType.REGISTER.getCode().equals(type)||UserCaptchaType.COMMIT_PHONE.getCode().equals(type)) {
            if (user != null) {
                UserException.raise(UserErrors.USER_PHONE_IS_EXITS);
            }
        } else if (UserCaptchaType.LOGIN.getCode().equals(type)) {
            //验证是都注册
            if (user == null) {
                UserException.raise(UserErrors.USER_NOT_FOUND);
            }
        }
        List<MobileCaptcha> mobileCaptchaList = mobileCaptchaMapper.selectByMobileAndType(mobile, type);

        // 判断是否超过每天最大发送次数
        if (mobileCaptchaList != null && mobileCaptchaList.size() >= maxSendCountOfDay) {
            UserException.raise(UserErrors.CAPTCHA_REQUEST_TOO_MUCH);
        }

        // 当天失败次数
        int failedCount = 0;
        // 最后一次发送时间
        long lastSendTime = 0L;

        // 计算失败次数和最后一次发送验证码时间(未使用的验证码发送时间)
        for (MobileCaptcha mobileCaptcha : mobileCaptchaList) {
            failedCount += mobileCaptcha.getFailures();
            if (mobileCaptcha.getUsedTime() == null && lastSendTime < mobileCaptcha.getCreatedTime().getTime()) {
                lastSendTime = mobileCaptcha.getCreatedTime().getTime();
            }
        }
        if (failedCount > this.maxFailedCount) {
            UserException.raise(UserErrors.CAPTCHA_ERROR_THAN_ACCEPTABLE);
        } else if (lastSendTime + sendCaptchaIntervalTime >= System.currentTimeMillis()) {
            UserException.raise(UserErrors.CAPTCHA_TOO_CLOSE_WITH_LAST_TIME);
        }

        return true;
    }

    @Override
    public CaptchaDto send(String mobile, String type) {
        CaptchaDto captchaDto = null;
        if (canSend(mobile, type)) {
            try {
                captchaDto = new CaptchaDto();
                CaptchaType captchaType = captchaTypeMapper.selectByTypeCode(type);

                // 创建验证码
                MobileCaptcha mobileCaptcha = new MobileCaptcha();
                mobileCaptcha.setCode(String.valueOf(Math.round(Math.random() * 899999 + 100000)));
                mobileCaptcha.setMobile(mobile);
                mobileCaptcha.setTypeId(captchaType.getId());
                Timestamp createdTime = new Timestamp(System.currentTimeMillis());
                mobileCaptcha.setFailures(0);
                mobileCaptcha.setCreatedTime(createdTime);
                mobileCaptcha.setExpiredTime(new Timestamp(createdTime.getTime() + captchaTimeOut));
                captchaDto.setExpired(mobileCaptcha.getExpiredTime());
                captchaDto.setCoolDown(captchaCoolDown);
                captchaDto.setCode(mobileCaptcha.getCode());
                mobileCaptchaMapper.insert(mobileCaptcha);
                // 发送验证码
                LOGGER.info("send code:{}", mobileCaptcha.getCode());
                if (!isBlockSendSMS) {
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("code", mobileCaptcha.getCode());
                    paramMap.put("mobile", mobileCaptcha.getMobile());
                    paramMap.put("templateId", captchaType.getSmsTemplateId().toString());
                    HttpUtil.sendJson(phpUrl+"smsSend",JSON.toJSONString(paramMap),new HashMap<>());
                }
            } catch (RuntimeException r) {
                UserException.raise(new ErrorCode("99999", r.getMessage()));
            } catch (Exception e) {
                LOGGER.error("send captcha failed.", e);
                UserException.raise(UserErrors.CAPTCHA_CANNOT_DELIVERY);
            }
        }

        return captchaDto;
    }

    @Override
    public boolean verify(String mobile, int type, String code) {
        long currTime = System.currentTimeMillis();
        boolean isValid = false;
        List<MobileCaptcha> mobileCaptchaList = mobileCaptchaMapper.selectNotUsedByMobileAndType(mobile, type);
        if (mobileCaptchaList == null || mobileCaptchaList.size() == 0) {
            UserException.raise(UserErrors.CAPTCHA_INPUT_ERROR);
        } else {
            MobileCaptcha lastSendMobileCaptcha = null;
            for (MobileCaptcha mobileCaptcha : mobileCaptchaList) {
                if (mobileCaptcha.getCode().equals(code)) {
                    if (mobileCaptcha.getExpiredTime().getTime() >= currTime) {
                        // 更新验证码状态为已使用
                        mobileCaptcha.setUsedTime(new Timestamp(currTime));
                        mobileCaptchaMapper.updateByPrimaryKey(mobileCaptcha);
                        isValid = true;
                        break;
                    } else {
                        UserException.raise(UserErrors.CAPTCHA_EXPIRED);
                    }
                }
                // 计算最近发送的验证码
                if (mobileCaptcha.getUsedTime() == null && lastSendMobileCaptcha == null) {
                    lastSendMobileCaptcha = mobileCaptcha;
                } else if (mobileCaptcha.getUsedTime() == null &&
                        lastSendMobileCaptcha.getExpiredTime().getTime() < mobileCaptcha.getExpiredTime().getTime()) {
                    lastSendMobileCaptcha = mobileCaptcha;
                }
            }
            if (!isValid) {
                if (lastSendMobileCaptcha != null) {
                    // 增加验证错误次数
                    mobileCaptchaMapper.incrFailuresById(lastSendMobileCaptcha.getId());
                }
                UserException.raise(UserErrors.CAPTCHA_INPUT_ERROR);
            }
        }
        return isValid;
    }
}
