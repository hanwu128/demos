package com.hw.blog.model;

/**
 * 网站管理表
 */
public class Option {

    private Long id;        //ID
    private String name;    //选项名称
    private String values;  //选项值

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

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

}
