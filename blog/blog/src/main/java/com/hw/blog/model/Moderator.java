package com.hw.blog.model;

/**
 * 版主表
 */
public class Moderator {

    private Long id;        //版主ID
    private Long forumId;   //论坛ID
    private String level;   //版主级别

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
