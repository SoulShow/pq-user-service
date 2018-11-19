package com.pq.user.dto;


/**
 * @author liutao
 */
public class RegisterRequestDto {
    private String phone;
    private String password;
    private int role;
    private int relation;
    private Boolean agree;
    private int requestFrom;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public int getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(int requestFrom) {
        this.requestFrom = requestFrom;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
