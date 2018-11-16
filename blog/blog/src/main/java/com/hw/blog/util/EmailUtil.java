package com.hw.blog.util;

import java.net.URL;

/**
 * 邮件工具类
 */
public class EmailUtil {

    /**
     * 获取文件完整路径的方法
     *
     * @param path 文件名称
     * @return URL 文件完整路径
     */
    public static URL findAsResource(String path) {
        URL url = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            url = contextClassLoader.getResource(path);
        }
        if (url != null)
            return url;
        url = EmailUtil.class.getClassLoader().getResource(path);
        if (url != null)
            return url;
        url = ClassLoader.getSystemClassLoader().getResource(path);
        return url;
    }

}
