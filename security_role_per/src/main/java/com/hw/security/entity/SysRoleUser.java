package com.hw.security.entity;

public class SysRoleUser {
    private Integer id;
    private Integer Sys_User_id;
    private Integer Sys_Role_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSys_User_id() {
        return Sys_User_id;
    }

    public void setSys_User_id(Integer sys_User_id) {
        Sys_User_id = sys_User_id;
    }

    public Integer getSys_Role_id() {
        return Sys_Role_id;
    }

    public void setSys_Role_id(Integer sys_Role_id) {
        Sys_Role_id = sys_Role_id;
    }
}
