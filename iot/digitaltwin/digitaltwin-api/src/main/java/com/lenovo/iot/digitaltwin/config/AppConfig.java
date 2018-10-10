package com.lenovo.iot.digitaltwin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @Desc: 应用程序配置
 * @Name: com.lenovo.iot.digitaltwin.config.AppConfig
 * @Author: chench9@lenovo.com
 * @Date: 2018/9/3
 */
@Configuration
public class AppConfig {

    /**
     * 加载国际化资源文件
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    }
}
