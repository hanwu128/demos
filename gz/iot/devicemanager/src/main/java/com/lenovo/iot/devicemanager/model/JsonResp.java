/**
 * 
 */
package com.lenovo.iot.devicemanager.model;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

/**
 * 通用json返回结果
 * @desc com.lenovo.iot.devicemanager.model.JsonResp
 * @author chench9@lenovo.com
 * @date 2017年11月6日
 */
public class JsonResp {
	public static final String MSG_SUCCESS = "success";
	public static final String MSG_ERROR = "error";
	
	/**
	 * 验证码错误
	 */
	public static final int SC_AUTHCODE_INVALID = 418;
	
	/**
	 * 密码为空
	 */
	public static final int SC_PASSWORD_EMPTY = 419;
	
	/**
	 * 账户未登录
	 */
	public static final int SC_NOT_LOGINED = 420;
	
	/**
	 * 密码不正确
	 */
	public static final int SC_PASSWORD_INCORRECT = 421;
	
	/**
	 * 账户已经认证
	 */
	public static final int SC_AUTHENTICATED = 423;
	
	/**
	 * 账户被冻结
	 */
	public static final int SC_DISABLED = 424;
	
	private int code = HttpServletResponse.SC_OK;
	private String message = "";
	private Object data = null;
	
	/**
	 * 返回指定参数json结果
	 * @param code
	 * @param message
	 * @param data
	 * @return
	 */
	public static JsonResp getJsonResp(int code, String message, Object data) {
		return new JsonResp(code, message, data);
	}
	
	/**
	 * 返回成功结果
	 * @param data
	 * @return
	 */
	public static JsonResp getJsonRespSuccess(Object data) {
		return new JsonResp(HttpServletResponse.SC_OK, MSG_SUCCESS, data);
	}
	
	/**
	 * 返回成功结果
	 * @return
	 */
	public static JsonResp getJsonRespSuccess() {
		return new JsonResp(HttpServletResponse.SC_OK, MSG_SUCCESS, MSG_SUCCESS);
	}

    /**
     * 返回错误结果
     * @param code
     * @return
     */
	public static JsonResp getJsonRespError(int code) {
        return new JsonResp(code, MSG_ERROR, MSG_ERROR);
    }
	
	/**
	 * 返回错误结果
	 * @param data
	 * @return
	 */
	public static JsonResp getJsonRespError(Object data) {
		return new JsonResp(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, MSG_ERROR, data);
	}
	
	/**
	 * 返回错误结果
	 * @return
	 */
	public static JsonResp getJsonRespError() {
		return new JsonResp(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, MSG_ERROR, MSG_ERROR);
	}
	
	/**
	 * 返回错误结果
	 * @param code
	 * @param data
	 * @return
	 */
	public static JsonResp getJsonRespError(int code, Object data) {
		return new JsonResp(code, MSG_ERROR, data);
	}
	
	private JsonResp() {
	}
	
	private JsonResp(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
