package com.pq.user.controller;

import com.pq.common.exception.CommonErrors;
import com.pq.user.dto.*;
import com.pq.user.entity.User;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.form.AuthForm;
import com.pq.user.form.ForgetPasswordForm;
import com.pq.user.service.*;
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
    @Autowired
    private MobileCaptchaService mobileCaptchaService;
    @Autowired
    private SessionService sessionService;

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

    @GetMapping("/captcha")
    @ResponseBody
    public UserResult<CaptchaDto> getCaptcha(@RequestParam(value = "mobile")String mobile,
                                             @RequestParam(value = "type")String type) {
        UserResult result = new UserResult();
        try {
            result.setData(mobileCaptchaService.send(mobile,type));
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

    @GetMapping("/captcha/verify")
    @ResponseBody
    public UserResult captchaVerify(@RequestParam(value = "mobile")String mobile,
                                                @RequestParam(value = "type")int type,
                                                @RequestParam(value = "code")String code) {
        UserResult result = new UserResult();
        try {
            mobileCaptchaService.verify(mobile,type,code);
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

    @PostMapping("/update/phone")
    @ResponseBody
    public UserResult updateUserPhone(@RequestBody UpdatePhoneDto updatePhoneDto) {
        UserResult result = new UserResult();
        try {
            User user = userService.getUserByPhone(updatePhoneDto.getAccount());
            user.setPhone(updatePhoneDto.getNewPhone());
            user.setUsername(updatePhoneDto.getNewPhone());
            userService.updateUserInfo(user);
            sessionService.deleteUserSession(user.getUserId(),updatePhoneDto.getSessionId());
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

    @PostMapping("/update/password")
    @ResponseBody
    public UserResult updateUserPassword(@RequestBody PasswordModifyDto passwordModifyDto) {
        UserResult result = new UserResult();
        try {
            User user = userService.getUserByUserId(passwordModifyDto.getUserId());

            userService.setPassword(user.getUserId(),user.getPhone(),
                    passwordModifyDto.getOldPassword(),passwordModifyDto.getRepPassword());
            sessionService.deleteUserSession(passwordModifyDto.getUserId(),passwordModifyDto.getSessionId());
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