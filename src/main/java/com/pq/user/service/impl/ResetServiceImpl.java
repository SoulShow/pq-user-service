package com.pq.user.service.impl;

import com.pq.common.util.DateUtil;
import com.pq.common.util.Password;
import com.pq.user.dto.UpdatePasswordParamsDto;
import com.pq.user.entity.User;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.mapper.UserMapper;
import com.pq.user.service.ResetService;
import com.pq.user.service.SessionService;
import com.pq.user.service.UserService;
import com.pq.user.utils.ConstantsUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResetServiceImpl implements ResetService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private Password passwordUtil;
    @Autowired
    private SessionService sessionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String phone, String newPassword, String repeatNewPassword,int role) {
        User userEntity = userMapper.selectByPhoneAndRole(phone,role);
        if (userEntity == null) {
            UserException.raise(UserErrors.USER_NOT_FOUND);
        }
        User originUserEntity = new User();
        BeanUtils.copyProperties(userEntity, originUserEntity);

        userEntity.setPassword(userService.encodePassword(newPassword));
        userEntity.setUpdatedTime(DateUtil.currentTime());

        userService.updateUser(userEntity, originUserEntity, ConstantsUser.USER_MODIFY_TYPE_REST_PASSWORD);
        userService.clearUserPasswordErrorLog(userEntity.getUserId());
        sessionService.deleteUserSession(userEntity.getUserId());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UpdatePasswordParamsDto updatePasswordParamsDto) {
        if (updatePasswordParamsDto.getOriginPassword().equals(updatePasswordParamsDto.getNewPassword())) {
            UserException.raise(UserErrors.USER_UPDATE_PASSWORD_SAME);
        }
        User userEntity = userMapper.selectByUserId(updatePasswordParamsDto.getUserId());
        User originUserEntity = new User();
        BeanUtils.copyProperties(userEntity, originUserEntity);

        if (!passwordUtil.checkPassword(updatePasswordParamsDto.getOriginPassword(), userEntity.getPassword())) {
            UserException.raise(UserErrors.USER_VERIFY_PASSWORD_ERROR);
        }
        userEntity.setPassword(userService.encodePassword(updatePasswordParamsDto.getNewPassword()));
        userEntity.setUpdatedTime(DateUtil.currentTime());

        userService.updateUser(userEntity, originUserEntity, ConstantsUser.USER_MODIFY_TYPE_PASSWORD);
    }

}
