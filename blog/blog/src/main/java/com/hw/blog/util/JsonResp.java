package com.hw.blog.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * Desc: JSON响应格式
 **/
public class JsonResp {
    public static String MSG_SUCCESS = "success";
    public static String MSG_ERROR = "server internal error";
    public static String MSG_NOT_FOUND = "resource not found";
    public static String MSG_PARAM_ERROR = "request parameter error";
    public static String MSG_FORBIDDEN = "has no permission";
    public static String MSG_RESOURCE_ID_ERROR = "resource id error";

    private static int CODE_SUCCESS = 0;
    private static int CODE_ERROR = 1001;

    private int code = CODE_SUCCESS;
    private String msg = MSG_SUCCESS;
    private String count;
    private Object data;

    private JsonResp() {
    }

    public JsonResp(Object data) {
        this.data = data;
    }

    public JsonResp(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResp(String count, Object data) {
        this.count = count;
        this.data = data;
    }

    public JsonResp(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResp(Integer code, String msg, String count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public static JsonResp getErrorResp() {
        return new JsonResp(CODE_ERROR, MSG_ERROR);
    }

    public static JsonResp getErrorResp(int code, String msg) {
        return new JsonResp(code, msg, new JSONObject());
    }

    public static JsonResp getErrorResp(int code, String msg, Object data) {
        return new JsonResp(code, msg, data);
    }

    public static JsonResp httpCode(HttpServletResponse resp, Integer code) {
        JsonResp jsonResp = new JsonResp();
        jsonResp.setCode(code);
        jsonResp.setMsg(MSG_SUCCESS);
        jsonResp.setData(new JSONObject());
        return jsonResp;
    }

    public static JsonResp getSuccessResp() {
        return new JsonResp(CODE_SUCCESS, MSG_SUCCESS);
    }

    public static JsonResp getSuccessResp(String count, Object data) {
        return new JsonResp(CODE_SUCCESS, MSG_SUCCESS, count, data);
    }

    public static JsonResp getSuccessResp(Object data) {
        return new JsonResp(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static JsonResp getSuccessResp(Object data, String msg) {
        return new JsonResp(CODE_SUCCESS, msg, data);
    }


    public JsonResp successResp(Object data) {
        this.setData(data);
        return this;
    }

    public JsonResp successResp(String count, Object data) {
        this.setCount(count);
        this.setData(data);
        return this;
    }

    public JsonResp errorResp(String msg) {
        this.setMsg(msg);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
