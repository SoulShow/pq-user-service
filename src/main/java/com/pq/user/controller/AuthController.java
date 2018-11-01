package com.pq.user.controller;

import com.pq.user.dto.UserDto;
import com.pq.user.form.AuthForm;
import com.pq.user.service.LoginService;
import com.pq.user.utils.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author liutao
 * @date
 */

@RestController
@RequestMapping("user")
public class AuthController  {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @ResponseBody
    public UserResult<UserDto> login(@RequestBody AuthForm authForm) {
        UserResult result = new UserResult();
        try {
            UserDto userDto = loginService.authentication(authForm);
            result.setData(userDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




}