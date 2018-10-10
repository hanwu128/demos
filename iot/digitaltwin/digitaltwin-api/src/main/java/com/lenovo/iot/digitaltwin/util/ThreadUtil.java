package com.lenovo.iot.digitaltwin.util;

import com.lenovo.iot.digitaltwin.model.ThreadUser;

/**
 * Desc: Thread工具类
 * Name: com.lenovo.iot.digitaltwin.filter.TokenUtil
 * Author: hanwu
 * Date: 2018/8/17 11:00
 **/
public class ThreadUtil {
    private static final ThreadUtil instance = new ThreadUtil();

    //token返回的用户信息存入ThreadLocal中
    private ThreadLocal<ThreadUser> t = new ThreadLocal<ThreadUser>();

    private ThreadUtil() {
    }

    public static ThreadUtil getThreadInstance() {
        return instance;
    }

    public void set(ThreadUser user) {
        t.set(user);
    }

    public ThreadUser get() {
        return t.get();
    }
}
