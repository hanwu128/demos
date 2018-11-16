package com.hw.blog.model;

import java.util.Date;

/**
 * 邮件表
 */
public class Email {

    private String id;          //邮件id，预留字段
    private String toMails;     //收件人
    private String copyMails;   //抄送人
    private String from;        //发送人
    private String content;     //内容
    private String[] archives;  //上传附件路径
    private String subject;     //主题
    private boolean containAttach; //是否带有附件
    private Date toDate;        //发送时间
    private String category;    //邮件分类  1、收件  2、发件  3、草稿  4、垃圾
    private String type;        //邮件类别

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToMails() {
        return toMails;
    }

    public void setToMails(String toMails) {
        this.toMails = toMails;
    }

    public String getCopyMails() {
        return copyMails;
    }

    public void setCopyMails(String copyMails) {
        this.copyMails = copyMails;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getArchives() {
        return archives;
    }

    public void setArchives(String[] archives) {
        this.archives = archives;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isContainAttach() {
        return containAttach;
    }

    public void setContainAttach(boolean containAttach) {
        this.containAttach = containAttach;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
