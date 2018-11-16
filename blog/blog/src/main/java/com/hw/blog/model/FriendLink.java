package com.hw.blog.model;

/**
 * 友链接
 */
public class FriendLink {
    private Long id;        // 友链ID
    private String links;   // 友链链接
    private String name;    // 友链名称
    private String description;// 友链描述
    private String logo;    // 友链Logo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
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
}
