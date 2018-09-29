package com.example.demo.model;

public class User {
    private Long id;
    private String name;
    private int age;
    private char sex;
    private String address;

    public User(String name, int age, char sex, String address) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.address = address;
    }

    public User() {
        super();
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
