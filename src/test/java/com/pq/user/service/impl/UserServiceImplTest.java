package com.pq.user.service.impl;

import com.google.gson.Gson;
import com.pq.common.util.Password;
import com.pq.user.entity.MobileCaptcha;
import com.pq.user.entity.User;
import com.pq.user.mapper.MobileCaptchaMapper;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    private MobileCaptchaService mobileCaptchaService;
    @Autowired
    private MobileCaptchaMapper mobileCaptchaMapper;
    @Autowired
    private Password passwordUtil;
    @Test
    public void getUserDtoByUserId() {
       Boolean password =  passwordUtil.checkPassword("123456","pbkdf2_sha256$10000$FLSH1D6BaIQ=$Pc5s9+0Ooc5nHf1MMH2hQqhhg1qYFdOopkCmBEI8/ec=");
       System.out.print(password);
    }

    @Test
    public void clearUserDtoCacheByUserId() {
    }

    @Test
    public void clearUserPasswordErrorLog() {
    }

    @Test
    public void transformUserEntityToUserDto() {
    }

    @Test
    public void encodePassword() {
    }

    @Test
    public void validate() {
    }

    @Test
    public void getUserByPhone() {
        User user = userService.getUserByPhone("18910845169",1);
        System.out.print("sss");
    }

    @Test
    public void register() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void updateUserInfo() {
    }

    @Test
    public void getUserByUserId() {
    }

    @Test
    public void getMiniProgramInfoSessionInfo() {
    }

    @Test
    public void getLastUserLoginLog() {
    }

    @Test
    public void setPassword() {
    }

    @Test
    public void insert() {
    }
}