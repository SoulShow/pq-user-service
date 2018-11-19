package com.pq.user.controller;

import com.pq.common.exception.CommonErrors;
import com.pq.user.dto.RegisterRequestDto;
import com.pq.user.dto.UserDto;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.form.AuthForm;
import com.pq.user.form.ForgetPasswordForm;
import com.pq.user.service.LoginService;
import com.pq.user.service.ResetService;
import com.pq.user.service.UserService;
import com.pq.user.utils.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author liutao
 * @date
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    @ResponseBody
    public UserResult<Integer> getUserInfo(@RequestParam(value = "userId")String userId) {
        UserResult result = new UserResult();
        try {
            result.setData(userService.getUserDtoByUserId(userId));
        } catch (UserException e){
            result.setStatus(e.getErrorCode().getErrorCode());
            result.setMessage(e.getErrorCode().getErrorMsg());
        }catch (Exception e) {
            e.printStackTrace();
            result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
            result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
        }
        return result;
    }


}