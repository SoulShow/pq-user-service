package com.pq.user.form;

import java.io.Serializable;

/**
 * @author liutao
 */
public class NameModifyForm implements Serializable {

    private static final long serialVersionUID = -1673612704736593281L;
    private String name;
    private String userId;
    private int role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
