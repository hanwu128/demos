package com.lemi.batisdemo2.entity;

public class Roles {
    private  Integer id;
    private String Rname;

    public Roles() {
    }

    public Roles(Integer id, String rname) {
        this.id = id;
        Rname = rname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRname() {
        return Rname;
    }

    public void setRname(String rname) {
        Rname = rname;
    }
}
