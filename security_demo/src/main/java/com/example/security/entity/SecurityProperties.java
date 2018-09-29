package com.example.security.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security 属性 类
 * Created by Fant.J.
 */
//获取配置属性前缀
@ConfigurationProperties(prefix = "fantJ.security")
public class SecurityProperties {
    /**
     * 浏览器 属性类
     */
    private BrowserProperties browser = new BrowserProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
