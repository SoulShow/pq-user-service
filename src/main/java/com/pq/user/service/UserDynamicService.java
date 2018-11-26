package com.pq.user.service;


import com.pq.user.dto.UserDynamicDto;
import com.pq.user.form.CancelPraiseDynamicForm;
import com.pq.user.form.PraiseDynamicForm;
import com.pq.user.form.UserDynamicCommentForm;
import com.pq.user.form.UserDynamicForm;

import java.util.List;

/**
 * 用户动态
 * @author liutao
 */
public interface UserDynamicService {

    /**
     * 获取动态列表
     * @param agencyClassId
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    List<UserDynamicDto> getUserDynamicList(Long agencyClassId,String userId, int offset,int size);


    /**
     * 发表动态
     * @param userDynamicForm
     * @return
     */
    void createDynamic(UserDynamicForm userDynamicForm);

    /**
     * 点赞
     * @param praiseDynamicForm
     * @return
     */
    Long praiseDynamic(PraiseDynamicForm praiseDynamicForm);

    /**
     * 取消点赞
     * @param cancelPraiseDynamicForm
     * @return
     */
    void cancelPraiseDynamic(CancelPraiseDynamicForm cancelPraiseDynamicForm);

    /**
     * 发表评论
     * @param dynamicCommentForm
     * @return
     */
    Long createDynamicComment(UserDynamicCommentForm dynamicCommentForm);
}
