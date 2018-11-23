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

    private static int CODE_SUCCESS = HttpServletResponse.SC_OK;
    private static int CODE_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    private int code = -1;
    private String msg = "";
    private Object data = null;

    private JsonResp() {
    }

    private JsonResp(int code) {
        this(code, "", new JSONObject());
    }

    private JsonResp(int code, String msg) {
        this(code, msg, new JSONObject());
    }

    public JsonResp(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
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

    public static JsonResp getSuccessResp() {
        return new JsonResp(CODE_SUCCESS, MSG_SUCCESS);
    }

    public static JsonResp getSuccessResp(Object data) {
        return new JsonResp(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static JsonResp getSuccessResp(Object data, String msg) {
        return new JsonResp(CODE_SUCCESS, msg, data);
    }

    ////////////////////////////////////////////////////////////////////////////
    public static JsonResp httpCode(HttpServletResponse resp, int code) {
        resp.setStatus(code);
        JsonResp jsonResp = new JsonResp(code);
        jsonResp.setMsg(MSG_SUCCESS);
        jsonResp.setData(new JSONObject());
        return jsonResp;
    }

    public JsonResp successResp(Object data) {
        this.setData(data);
        return this;
    }

    public JsonResp errorResp(String msg) {
        this.setMsg(msg);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
