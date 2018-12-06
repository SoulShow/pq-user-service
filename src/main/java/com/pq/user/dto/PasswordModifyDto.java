package com.pq.user.dto;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改密码的表单
 * @author liutao
 */
public class PasswordModifyDto implements Serializable {

    private static final long serialVersionUID = 7790325968489843402L;
    private String userId;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    private String repPassword;

    private String sessionId;

    private int role;

    @NotBlank(message = "请输入原登录密码")
    @NotNull(message = "请输入原登录密码")
    @Size(min = 6, max = 12, message = "密码由6-12位英文字母、数字或符号组成。")
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }


    @NotBlank(message = "请输入新登录密码")
    @NotNull(message = "请输入新登录密码")
    @Size(min = 6, max = 12, message = "密码由6-12位英文字母、数字或符号组成。")
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    @NotBlank(message = "请输入确认登录密码")
    @NotNull(message = "请输入确认登录密码")
    @Size(min = 6, max = 12, message = "密码由6-12位英文字母、数字或符号组成。")
    public String getRepPassword() {
        return repPassword;
    }

    public void setRepPassword(String repPassword) {
        this.repPassword = repPassword;
    }

    /**
     * 新密码必须不同
     *
     * @return
     */
    @AssertTrue(message = "新密码不能跟旧密码相同")
    public boolean isNewPasswordShouldBeDifferent() {
        return !StringUtils.equals(newPassword, oldPassword);
    }

    /**
     * 确认密码必须相同
     *
     * @return
     */
    @AssertTrue(message = "请确保密码保持一致！")
    public boolean isSamePassword() {
        return StringUtils.equals(newPassword, oldPassword);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
