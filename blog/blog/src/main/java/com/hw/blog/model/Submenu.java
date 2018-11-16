package com.hw.blog.model;

/**
 * 子菜单表
 */
public class Submenu {
    private Long id;        //子菜单ID
    private Long menuId;    //总菜单ID
    private String name;    //子菜单名称
    private String target;  //子菜单链接
    private String openWay; //子菜单打开方式
    private Long parentId;  //父菜单

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOpenWay() {
        return openWay;
    }

    public void setOpenWay(String openWay) {
        this.openWay = openWay;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
