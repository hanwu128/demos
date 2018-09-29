package com.hw.security.config;

import com.hw.security.until.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder {

    /**
     * 对输入的密码进行加密
     * @param arg0
     * @return
     */
    @Override
    public String encode(CharSequence arg0) {
        return MD5Util.encode(arg0.toString());
    }

    /**
     * 密码比较
     * @param arg0 用户输入密码
     * @param arg1 数据库保存秘密
     * @return
     */
    @Override
    public boolean matches(CharSequence arg0, String arg1) {
        return MD5Util.encode(arg1).equals(MD5Util.encode(arg0.toString()));
    }
}
