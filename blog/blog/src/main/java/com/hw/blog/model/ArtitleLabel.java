package com.hw.blog.model;

/**
 * 文章设置标签表
 */
public class ArtitleLabel {

    private Long articleId; //文章ID
    private Long labelId;   //标签ID

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }
}
