package com.hw.blog.model;

/**
 * 文章设置分类表
 */
public class ArtitleSort {

    private Long articleId; // 文章ID
    private Long sortId;    // 分类ID

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getSortId() {
        return sortId;
    }

    public void setSortId(Long sortId) {
        this.sortId = sortId;
    }
}
