package com.hw.blog.model;

/**
 * 论坛表
 */
public class Forum {

    private Long id;        // 论坛ID
    private String name;    // 论坛名称
    private String description;// 论坛描述
    private String logo;    // 论坛Logo
    private Long postCount; // 论坛帖子数
    private Long parentId;  // 父论坛ID

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
