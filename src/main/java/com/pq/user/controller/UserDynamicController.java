package com.pq.user.controller;

import com.pq.common.exception.CommonErrors;
import com.pq.user.dto.*;
import com.pq.user.entity.User;
import com.pq.user.entity.UserDynamic;
import com.pq.user.exception.UserException;
import com.pq.user.form.*;
import com.pq.user.service.MobileCaptchaService;
import com.pq.user.service.SessionService;
import com.pq.user.service.UserDynamicService;
import com.pq.user.service.UserService;
import com.pq.user.utils.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author liutao
 * @date
 */

@RestController
@RequestMapping("/user/dynamic")
public class UserDynamicController {
    @Autowired
    private UserDynamicService dynamicService;

    @GetMapping("")
    @ResponseBody
    public UserResult<List<UserDynamicDto>> getUserDynamic(@RequestParam(value = "agencyClassId")Long agencyClassId,
                                                           @RequestParam(value = "userId")String userId,
                                                           @RequestParam(value = "studentId",required = false) Long studentId,
                                                           @RequestParam(value = "page",required = false)Integer page,
                                                           @RequestParam(value = "size",required = false)Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        int offset = (page - 1) * size;

        UserResult result = new UserResult();
        try {
            result.setData(dynamicService.getUserDynamicList(agencyClassId,studentId,userId,offset,size));
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

    @GetMapping("/detail")
    @ResponseBody
    public UserResult<UserDynamicDto> getUserDynamicDetail(@RequestParam(value = "userId")String userId,
                                                           @RequestParam(value = "studentId",required = false) Long studentId,
                                                           @RequestParam(value = "dynamicId") Long dynamicId,
                                                           @RequestParam(value = "commentId",required = false) Long commentId) {

        UserResult result = new UserResult();
        try {
            result.setData(dynamicService.getUserDynamicDetail(studentId, dynamicId, commentId, userId));
        } catch (UserException e) {
            result.setStatus(e.getErrorCode().getErrorCode());
            result.setMessage(e.getErrorCode().getErrorMsg());
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
            result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @PostMapping("")
    @ResponseBody
    public UserResult createDynamic(@RequestBody UserDynamicForm userDynamicForm) {
        UserResult result = new UserResult();
        try {
            dynamicService.createDynamic(userDynamicForm);
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

    @PostMapping("/praise")
    @ResponseBody
    public UserResult praiseDynamic(@RequestBody PraiseDynamicForm praiseDynamicForm) {
        UserResult result = new UserResult();
        try {
            result.setData(dynamicService.praiseDynamic(praiseDynamicForm));
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
    @PostMapping("/cancel/praise")
    @ResponseBody
    public UserResult cancelPraiseDynamic(@RequestBody CancelPraiseDynamicForm cancelPraiseDynamicForm) {
        UserResult result = new UserResult();
        try {
            result.setData(dynamicService.cancelPraiseDynamic(cancelPraiseDynamicForm));
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
    @PostMapping("/comment")
    @ResponseBody
    public UserResult createDynamicComment(@RequestBody UserDynamicCommentForm dynamicCommentForm) {
        UserResult result = new UserResult();
        try {
            result.setData(dynamicService.createDynamicComment(dynamicCommentForm));
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
    @PostMapping("delete")
    @ResponseBody
    public UserResult deleteDynamic(@RequestBody UserDynamicDelForm dynamicDelForm) {
        UserResult result = new UserResult();
        try {
            dynamicService.deleteDynamic(dynamicDelForm.getDynamicId(),dynamicDelForm.getUserId(),
                    dynamicDelForm.getStudentId(),dynamicDelForm.getRole());
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

    @GetMapping("/message/list")
    @ResponseBody
    public UserResult<List<CommentMessageDto>> getUserDynamicMessageList(@RequestParam(value = "agencyClassId")Long agencyClassId,
                                                                         @RequestParam(value = "studentId",required = false) Long studentId,
                                                                         @RequestParam(value = "userId") String userId,
                                                                         @RequestParam(value = "role") int role,
                                                                         @RequestParam(value = "page",required = false)Integer page,
                                                                         @RequestParam(value = "size",required = false)Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        int offset = (page - 1) * size;

        UserResult result = new UserResult();
        try {
            result.setData(dynamicService.getCommentMessageList(studentId,agencyClassId,userId,role,offset,size));
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