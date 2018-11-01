package com.pq.user.service.impl;

import com.pq.user.entity.User;
import com.pq.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Test
    public void getUserDtoByUserId() {
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