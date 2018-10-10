package com.lenovo.iot.digitaltwin.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc: 工具类
 * Name: com.lenovo.iot.digitaltwin.util.DBUtil
 * Author: chench9@lenovo.com
 * Date: 2018/6/7 15:54
 **/
public class DBUtil {
    /**
     * 构造模糊查询条件
     * @param str
     * @return
     */
    public static String like(String str) {
        if(str == null || "".equals(str.trim())) {
            return "%";
        }
        return "%" + str + "%";
    }

    /**
     * 构建根据名称查询的参数
     * @param name
     * @return
     */
    public static Map<String, Object> buildNameParam(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        return params;
    }

    /**
     * 构造根据名称和分页条件查询的参数
     * @param name
     * @param start
     * @param offset
     * @return
     */
    public static Map<String, Object> buildNamePageParam(String name, int start, int offset,String uid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("start", start);
        params.put("offset", offset);
        params.put("uid",uid);
        return params;
    }

    private DBUtil() {
    }
}
