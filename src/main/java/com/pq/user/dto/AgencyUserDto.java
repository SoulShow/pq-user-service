package com.pq.user.dto;

public class AgencyUserDto {

    private Long agencyClassId;

    private String userId;

    private Long studentId;

    private Integer role;

    private Integer relation;

    private String studentName;

    private String name;

    public Long getAgencyClassId() {
        return agencyClassId;
    }

    public void setAgencyClassId(Long agencyClassId) {
        this.agencyClassId = agencyClassId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}