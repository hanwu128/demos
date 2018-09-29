package com.hw.security.entity;

import java.util.List;

public class SysRole {
    private Integer id;
    private String name;
    private List<Syspermission> permissions;

    public SysRole() {
        super();
    }

    public SysRole(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Syspermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Syspermission> permissions) {
        this.permissions = permissions;
    }
}
