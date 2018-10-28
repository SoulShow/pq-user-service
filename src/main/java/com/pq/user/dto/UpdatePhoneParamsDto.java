package com.pq.user.dto;

public class UpdatePhoneParamsDto {
    private UserDto userDto;
    private String tempToken;
    private String newPhone;
    private String verifyCode;
    private String password;
    private Long cpId;
    private boolean isOldPhoneNotUse;


    public UserDto getUserDto () {
        return userDto;
    }

    public void setUserDto (UserDto userDto) {
        this.userDto = userDto;
    }

    public String getTempToken () {
        return tempToken;
    }

    public void setTempToken (String tempToken) {
        this.tempToken = tempToken;
    }

    public String getNewPhone () {
        return newPhone;
    }

    public void setNewPhone (String newPhone) {
        this.newPhone = newPhone;
    }

    public String getVerifyCode () {
        return verifyCode;
    }

    public void setVerifyCode (String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public Boolean isOldPhoneNotUse() {
        return isOldPhoneNotUse;
    }

    public void setIsOldPhoneNotUse(Boolean isOldPhoneNotUse) {
        this.isOldPhoneNotUse = isOldPhoneNotUse;
    }
}
