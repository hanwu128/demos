package com.hw.blog.model;

/**
 * 标签表
 */
public class Label {

    private Long id;        //标签ID
    private String name;    // 标签名称
    private String alias;   // 标签别名
    private String description;// 标签描述

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
