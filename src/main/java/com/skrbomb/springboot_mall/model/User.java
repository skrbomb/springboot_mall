package com.skrbomb.springboot_mall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class User {
    private Integer userId;
    private String email;

    //回傳時隱藏密碼: SpringBoot 在轉換這個User Object為json格式時會忽略該變數
    @JsonIgnore
    private String password;

    private Date createdDate;
    private Date lastModifiedDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
