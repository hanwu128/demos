package com.hw.blog.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 常用链接配置类
 */
@Component
@PropertySource("classpath:/mail.properties")
public class Config {

    @Value("${MAIL_SMTP_HOST}")
    private String MAIL_SMTP_HOST;
    @Value("${MAIL_SMTP_PORT}")
    private Integer MAIL_SMTP_PORT;
    @Value("${MAIL_SMTP_AUTH}")
    private String MAIL_SMTP_AUTH;

    @Value("${MAIL_POP3_HOST}")
    public String MAIL_POP3_HOST;
    @Value("${MAIL_POP3_PORT}")
    public Integer MAIL_POP3_PORT;
    @Value("${MAIL_POP3_TYPE}")
    public String MAIL_POP3_TYPE;
    @Value("${MAIL_POP3_AUTH}")
    public String MAIL_POP3_AUTH;

    @Value("${MAIL_ATTACH_PATH}")
    public String MAIL_ATTACH_PATH;

    @Value("${MAIL_USER}")
    private String MAIL_USER;
    @Value("${MAIL_PWD}")
    private String MAIL_PWD;

    public String getMAIL_SMTP_HOST() {
        return MAIL_SMTP_HOST;
    }

    public void setMAIL_SMTP_HOST(String MAIL_SMTP_HOST) {
        this.MAIL_SMTP_HOST = MAIL_SMTP_HOST;
    }

    public Integer getMAIL_SMTP_PORT() {
        return MAIL_SMTP_PORT;
    }

    public void setMAIL_SMTP_PORT(Integer MAIL_SMTP_PORT) {
        this.MAIL_SMTP_PORT = MAIL_SMTP_PORT;
    }

    public String getMAIL_SMTP_AUTH() {
        return MAIL_SMTP_AUTH;
    }

    public void setMAIL_SMTP_AUTH(String MAIL_SMTP_AUTH) {
        this.MAIL_SMTP_AUTH = MAIL_SMTP_AUTH;
    }

    public String getMAIL_POP3_HOST() {
        return MAIL_POP3_HOST;
    }

    public void setMAIL_POP3_HOST(String MAIL_POP3_HOST) {
        this.MAIL_POP3_HOST = MAIL_POP3_HOST;
    }

    public Integer getMAIL_POP3_PORT() {
        return MAIL_POP3_PORT;
    }

    public void setMAIL_POP3_PORT(Integer MAIL_POP3_PORT) {
        this.MAIL_POP3_PORT = MAIL_POP3_PORT;
    }

    public String getMAIL_POP3_TYPE() {
        return MAIL_POP3_TYPE;
    }

    public void setMAIL_POP3_TYPE(String MAIL_POP3_TYPE) {
        this.MAIL_POP3_TYPE = MAIL_POP3_TYPE;
    }

    public String getMAIL_POP3_AUTH() {
        return MAIL_POP3_AUTH;
    }

    public void setMAIL_POP3_AUTH(String MAIL_POP3_AUTH) {
        this.MAIL_POP3_AUTH = MAIL_POP3_AUTH;
    }

    public String getMAIL_ATTACH_PATH() {
        return MAIL_ATTACH_PATH;
    }

    public void setMAIL_ATTACH_PATH(String MAIL_ATTACH_PATH) {
        this.MAIL_ATTACH_PATH = MAIL_ATTACH_PATH;
    }

    public String getMAIL_USER() {
        return MAIL_USER;
    }

    public void setMAIL_USER(String MAIL_USER) {
        this.MAIL_USER = MAIL_USER;
    }

    public String getMAIL_PWD() {
        return MAIL_PWD;
    }

    public void setMAIL_PWD(String MAIL_PWD) {
        this.MAIL_PWD = MAIL_PWD;
    }
}
