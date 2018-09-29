package com.hw.security5.entity;

public class Permission {
    private Integer id;
    private Integer rolesId;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
