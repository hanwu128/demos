package com.tsdb.demo.tsdb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Desc: 通用工具集
 */
public class CommonUtil {

    /**
     * URL编码
     */
    public static String urlEncode(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }

        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private CommonUtil() {
    }
}
