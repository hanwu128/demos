package com.hw.blog.model;

import java.io.Serializable;

/**
 * 用户表
 */
public class User implements Serializable {

    private Long id;        //ID
    private String ip;      //IP
    private String name;    //用户名
    private String password;//用户密码
    private String email;   //用户邮箱
    private String photo;//用户头像
    private String level = "菜鸟";   //用户等级
    private String permission = "普通用户";  //用户权限
    private Long registTime;//注册时间
    private String birth;  //用户生日
    private Integer age;    //用户年龄
    private String phone;//用户手机号
    private String nickname;//用户昵称
    private Integer activate = 0;//激活状态  0未激活  1已激活
    private String code;    //注册码
    private Integer del = 0;    //删除标识  0未删除   1已删除

    public User() {
        super();
    }

    public User(Long id, String ip, String name, String email, String photo, String level, Long registTime, String birth, Integer age, String phone, String nickname) {
        this.id = id;
        this.ip = ip;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.level = level;
        this.registTime = registTime;
        this.birth = birth;
        this.age = age;
        this.phone = phone;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Long registTime) {
        this.registTime = registTime;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getActivate() {
        return activate;
    }

    public void setActivate(Integer activate) {
        this.activate = activate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }
}
