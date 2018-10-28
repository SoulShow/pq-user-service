package com.pq.user.service;


import com.pq.user.dto.UpdatePasswordParamsDto;

/**
 * @author liutao
 */
public interface ResetService {

    /**
     * 重置密码
     *
     * @param phone
     * @param newPassword
     * @param repeatNewPassword
     */
    void resetPassword(String phone, String newPassword, String repeatNewPassword);

    /**
     * 修改用户密码
     *
     * @param updatePasswordParamsDto
     */
    void updatePassword(UpdatePasswordParamsDto updatePasswordParamsDto);

}
