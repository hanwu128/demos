package com.example.security.until;

/**
 * 返回类 工具 （可返回任意类型的结果）
 * Created by Fant.J.
 */
public class SimpleResponse {

    /**
     * 返回 内容 （json格式）
     */
    private Object content;

    public SimpleResponse(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

}
