package com.hw.blog.model;

/**
 * 分类表
 */
public class Sort {
    private Long id;        // 分类ID
    private String name;    // 分类名称
    private String alias;   // 分类别名
    private String description;// 分类描述
    private Long parentId;  // 父分类ID

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
