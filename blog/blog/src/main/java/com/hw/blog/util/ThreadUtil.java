package com.hw.blog.util;

import com.hw.blog.model.LoginUser;

/**
 * 保存用户的登录信息
 */
public class ThreadUtil {

    private static final ThreadUtil instance = new ThreadUtil();

    //用户登陆信息存入ThreadLocal中
    private static ThreadLocal<LoginUser> t = new ThreadLocal<LoginUser>();

    private ThreadUtil() {
    }

    public static ThreadUtil getThreadInstance() {
        return instance;
    }

    public void set(LoginUser user) {
        t.set(user);
    }

    public static LoginUser get() {
        return t.get();
    }

}
