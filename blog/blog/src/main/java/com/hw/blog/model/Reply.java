package com.hw.blog.model;

import java.util.Date;

/**
 * 回帖表
 */
public class Reply {

    private Long id;    // 回帖ID
    private Long userId;// 回帖用户ID
    private Long postId;// 所属主贴ID
    private String content;// 回帖内容
    private Date date;  // 回帖时间
    private Long parentId;// 父回帖ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
