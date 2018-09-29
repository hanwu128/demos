package com.hw.security5.entity;

public class UserRole {
    private Long id;
    private Integer sysUserId;
    private Integer rolesId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }
}
