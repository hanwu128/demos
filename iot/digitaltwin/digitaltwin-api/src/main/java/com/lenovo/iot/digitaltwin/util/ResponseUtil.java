package com.lenovo.iot.digitaltwin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Desc: Response工具类
 * Name: com.lenovo.iot.digitaltwin.filter.ResponseUtil
 * Author: hanwu
 * Date: 2018/8/20 15:00
 **/
public class ResponseUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    /**
     * 将后台处理完的结果写回前端页面
     *
     * @param response
     * @param obj
     */
    public static void write(HttpServletResponse response, Object obj) {
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(String.valueOf(obj));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
