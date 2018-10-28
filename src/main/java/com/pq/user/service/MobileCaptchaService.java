package com.pq.user.service;

import com.pq.user.dto.CaptchaDto;

/**
 * @author liutao
 */
public interface MobileCaptchaService {

    /**
     * 验证该用户是否可以发送验证码
     *
     * @param mobile 手机号
     * @param type   验证码类型
     * @return
     */
    boolean canSend(String mobile, String type);

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @param type   验证码类型
     * @return
     */
    CaptchaDto send(String mobile, String type);


    /**
     * 校验验证码
     *
     * @param mobile 手机号
     * @param type   验证码类型
     * @param code   验证码
     * @return
     */
    boolean verify(String mobile, String type, String code);
}
