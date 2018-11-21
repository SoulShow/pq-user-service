package com.pq.user.controller;

import com.pq.common.exception.CommonErrors;
import com.pq.user.dto.RegisterRequestDto;
import com.pq.user.dto.UserDto;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.form.AuthForm;
import com.pq.user.form.ForgetPasswordForm;
import com.pq.user.form.LogoutForm;
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
public class AuthController  {
    @Autowired
    private LoginService loginService;
    @Autowired
    private ResetService resetService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public UserResult<UserDto> login(@RequestBody AuthForm authForm) {
        UserResult result = new UserResult();
        try {
            UserDto userDto = loginService.authentication(authForm);
            result.setData(userDto);
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

    @PostMapping("/logout")
    @ResponseBody
    public UserResult login(@RequestBody LogoutForm logoutForm) {
        UserResult result = new UserResult();
        try {
            loginService.logout(logoutForm.getUserId(),logoutForm.getSessionId());
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
    @PostMapping("/forgetPassword")
    @ResponseBody
    public UserResult<UserDto> forgetPassword(@RequestBody ForgetPasswordForm forgetPasswordForm) {
        UserResult result = new UserResult();

        try {
            if (!forgetPasswordForm.isValidMobile()){
                UserException.raise(UserErrors.REGISTER_ERROR_MOBILE);
            }
            if(!forgetPasswordForm.isPasswordMatch()){
                UserException.raise(UserErrors.USER_PASSWORD_MODIFY_NOT_SANME_ERROR);
            }
            resetService.resetPassword(forgetPasswordForm.getAccount(),forgetPasswordForm.getNewPassword(),forgetPasswordForm.getRepPassword());
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
    @GetMapping("/login/try")
    @ResponseBody
    public UserResult<Integer> forgetPassword(@RequestParam(value = "mobile")String mobile) {
        UserResult result = new UserResult();

        try {
            loginService.loginTryTimes(mobile);
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
    @PostMapping("/register")
    @ResponseBody
    public UserResult<UserDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        UserResult result = new UserResult();

        try {
            String userId = userService.register(registerRequestDto);
            result.setData(userId);
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