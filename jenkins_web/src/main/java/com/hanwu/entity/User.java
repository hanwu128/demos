package com.hanwu.entity;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private char sex;
    private int age;
    private String address;

    public User() {
        super();
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String username, String password, char sex, int age, String address) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
