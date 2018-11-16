package com.hw.blog.model;

/**
 * 发件人表
 */
public class SendEmail {

    private Long id;        //邮件ID
    private String email;   //发件人邮箱
    private String password;//邮箱密码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
