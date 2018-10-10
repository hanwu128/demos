package com.lenovo.iot.digitaltwin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @Desc: 读取国际化资源文件
 * @Name: com.lenovo.iot.digitaltwin.service.MessagesUtil
 * @Author: chench9@lenovo.com
 * @Date: 2018/9/3
 */
@Component
public class MessagesService {

    @Autowired
    private MessageSource messageSource;

    /**
     * 读取指定消息
     * @param key
     * @param params
     * @return
     */
    public String getMessage(String key, Object[] params) {
        Locale locale = Locale.getDefault();
        return messageSource.getMessage(key, params, locale);
    }

    public String getMessage(String key) {
        return getMessage(key, null);
    }

    public MessagesService() {
    }
}
