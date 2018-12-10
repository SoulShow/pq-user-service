package com.pq.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liutao
 */
public class UserDto implements Serializable {

    private String username;
    private String huanxinId;
    private String picture;
    private String phone;
    private int role;
    private String address;
    private String userId;
    private List<AgencyUserDto> studentList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<AgencyUserDto> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<AgencyUserDto> studentList) {
        this.studentList = studentList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHuanxinId() {
        return huanxinId;
    }

    public void setHuanxinId(String huanxinId) {
        this.huanxinId = huanxinId;
    }
}
