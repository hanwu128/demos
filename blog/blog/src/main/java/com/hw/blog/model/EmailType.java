package com.hw.blog.model;

/**
 * 邮件类别表
 */
public class EmailType {

    private Long id;        //类别ID
    private String name;    //类别名称

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
}
