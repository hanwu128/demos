package com.hw.blog.model;

/**
 * 角色表
 */
public class Role {
    private Long id;    //角色ID
    private String name;//角色名称
    private String descritpion;//角色描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }
}
