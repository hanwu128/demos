package com.hw.security.entity;

import java.util.List;

public class SysUser {
    private Integer id;
    private String username;
    private String password;
    private List<SysRole> roles;

    public SysUser() {
        super();
    }

    public SysUser(Integer id, String name, String password) {
        super();
        this.id = id;
        this.username = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}
