package com.hw.blog.util;

import com.hw.blog.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 保存用户的登录信息
 */
public class ThreadUtil {
    private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);

    private static final ThreadUtil instance = new ThreadUtil();

    //用户登陆信息存入ThreadLocal中
    private static ThreadLocal<LoginUser> t = new ThreadLocal<LoginUser>();

    private ThreadUtil() {
    }

    public static ThreadUtil getThreadInstance() {
        return instance;
    }

    public static void set(LoginUser user) {
        t.set(user);
    }

    public static LoginUser get() {
        return t.get();
    }

    /**
     * 在ThreadLocal中取token
     *
     * @return
     */
    public static String getToken() {
        String token = null;
        if (get() == null || StringUtils.isEmpty(get().getToken())) {
            logger.error("token is null");
            return null;
        } else {
            token = get().getToken();
        }
        return token;
    }

    /**
     * 在ThreadLocal中取id
     *
     * @return
     */
    public static Long getId() {
        Long id = null;
        if (get() == null || get().getId() == null || get().getId() <= 0) {
            logger.error("id is null");
            return null;
        } else {
            id = get().getId();
        }
        return id;
    }

}
