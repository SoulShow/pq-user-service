package com.pq.user.controller;

import com.pq.common.captcha.UserCaptchaType;
import com.pq.common.exception.CommonErrors;
import com.pq.user.dto.*;
import com.pq.user.entity.User;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.form.FeedbackForm;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.service.SessionService;
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
    @Autowired
    private MobileCaptchaService mobileCaptchaService;
    @Autowired
    private SessionService sessionService;

    @GetMapping("")
    @ResponseBody
    public UserResult<UserDto> getUserInfo(@RequestParam(value = "userId")String userId) {
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

    @GetMapping("/info")
    @ResponseBody
    public UserResult<User> getUserBaseInfo(@RequestParam(value = "userId")String userId) {
        UserResult result = new UserResult();
        try {
            result.setData(userService.getUserByUserId(userId));
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
                                             @RequestParam(value = "type")String type,
                                             @RequestParam(value = "role")int role) {
        UserResult result = new UserResult();
        try {
            result.setData(mobileCaptchaService.send(mobile,type,role));
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
            User user = userService.getUserByPhone(updatePhoneDto.getAccount(),updatePhoneDto.getRole());
            User newUser = userService.getUserByPhone(updatePhoneDto.getNewPhone(),updatePhoneDto.getRole());
            if(newUser!=null){
                UserException.raise(UserErrors.USER_PHONE_IS_EXITS);
            }
            mobileCaptchaService.verify(updatePhoneDto.getNewPhone(),UserCaptchaType.COMMIT_PHONE.getIndex(),updatePhoneDto.getVerCode());
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
                    passwordModifyDto.getOldPassword(),passwordModifyDto.getRepPassword(),passwordModifyDto.getRole());
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
    @PostMapping("/update/avatar")
    @ResponseBody
    public UserResult updateUserAvatar(@RequestBody UserModifyDto userModifyDto) {
        UserResult result = new UserResult();
        try {
            User user = userService.getUserByUserId(userModifyDto.getUserId());
            user.setAvatar(userModifyDto.getAvatar());
            userService.updateUserInfo(user);
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

    @PostMapping("/update/address")
    @ResponseBody
    public UserResult updateUserAddress(@RequestBody UserModifyDto userModifyDto) {
        UserResult result = new UserResult();
        try {
            User user = userService.getUserByUserId(userModifyDto.getUserId());
            user.setAddress(userModifyDto.getAddress());
            userService.updateUserInfo(user);
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

    @PostMapping("/feedback")
    @ResponseBody
    public UserResult feedback(@RequestBody FeedbackForm feedbackForm) {
        UserResult result = new UserResult();
        try {
            userService.feedback(feedbackForm.getUserId(),feedbackForm.getContent());
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