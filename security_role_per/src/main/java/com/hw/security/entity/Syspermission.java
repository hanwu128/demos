package com.hw.security.entity;

import java.util.List;

public class Syspermission {
    private Integer id;
    private String name;
    private String description;
    private String url;
    private Integer pid;
    private String method;

    public Syspermission() {
        super();
    }

    public Syspermission(Integer id, String name, String description, String url, Integer pid, String method) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.pid = pid;
        this.method = method;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
