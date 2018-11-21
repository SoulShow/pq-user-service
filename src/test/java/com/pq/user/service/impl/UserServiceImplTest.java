package com.pq.user.service.impl;

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
    @Test
    public void getUserDtoByUserId() {
        List<MobileCaptcha> list =  mobileCaptchaMapper.selectByMobileAndType("18000000000","user:updatePhone");

        mobileCaptchaService.send("18000000000","user:updatePhone");
        System.out.print("");
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
        User user = userService.getUserByPhone("18910845169");
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