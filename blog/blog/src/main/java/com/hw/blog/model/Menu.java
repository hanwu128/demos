package com.hw.blog.model;

/**
 * 总菜单表
 */
public class Menu {

    private Long menu_id;//总菜单
    private String menu_name;//总菜单名称

    public Long getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(Long menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
}
