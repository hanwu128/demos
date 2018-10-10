package com.lenovo.iot.digitaltwin.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * Desc: JSON响应格式
 * Name: com.lenovo.iot.digitaltwin.util.JsonResp
 * Author: chench9@lenovo.com
 * Date: 2018/6/7 16:16
 **/
public class JsonResp {
    public static String MSG_SUCCESS = "success";
    public static String MSG_ERROR = "server internal error";
    public static String MSG_NOT_FOUND = "resource not found";
    public static String MSG_PARAM_ERROR = "request parameter error";

    private static int CODE_SUCCESS = HttpServletResponse.SC_OK;
    private static int CODE_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    public static int CODE_ATTR_NAME_CONFLICT = 419;    //物模板属性名称相同返回状态码
    public static int CODE_ATTR_VALUE_FORMAT_INVALID = 420;   //物模板属性value值与数据类型不一致
    public static int CODE_NOT_DELETE = 510;            //物实例正在使用不允许删除状态码

    private int code = -1;
    private String message = "";
    private Object data = null;

    private JsonResp() {
    }

    private JsonResp(int code) {
        this(code, "", new JSONObject());
    }

    private JsonResp(int code, String msg) {
        this(code, msg, new JSONObject());
    }

    private JsonResp(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static JsonResp getErrorResp() {
        return new JsonResp(CODE_ERROR, MSG_ERROR);
    }

    public static JsonResp getErrorResp(int code, String msg) {
        return new JsonResp(code, msg, new JSONObject());
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
        jsonResp.setMessage(MSG_SUCCESS);
        jsonResp.setData(new JSONObject());
        return jsonResp;
    }

    public JsonResp successResp(Object data) {
        this.setData(data);
        return this;
    }

    public JsonResp errorResp(String msg) {
        this.setMessage(msg);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
