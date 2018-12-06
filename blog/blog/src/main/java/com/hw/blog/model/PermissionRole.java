package com.hw.blog.model;

/**
 * 角色权限表
 */
public class PermissionRole {

    private Long id;    //ID
    private Long permissionId;//权限ID
    private Long roleId;//角色ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
