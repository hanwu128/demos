package com.hw.blog.model;

/**
 * 角色用户表
 */
public class RoleUser {
    private Long id;    //ID
    private Long userId;//用户ID
    private Long roleId;//角色ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
