/**
 * 
 */
package com.lenovo.iot.devicemanager.util.httppost;

import java.util.Map;

/**
 * 调度实例http实现
 * 
 * @desc com.lenovo.opinion.taskmgr.task.ScheduledInstanceHttp
 * @author chench9@lenovo.com
 * @date 2016年12月15日
 */
public class ScheduledInstanceHttp extends ScheduledInstance {
	private String method = "GET"; // HTTP方法名
	private String url = null; // 目标地址
	private Map<String, Object> headers = null; // 消息头
	private byte[] body = null; // 消息体
	
	public ScheduledInstanceHttp(String method, String url) {
		this(method, url, null, null);
	}
	
	public ScheduledInstanceHttp(String method, byte[] body) {
		this(method, null, null, body);
	}
	
	public ScheduledInstanceHttp(String method, String url, Map<String, Object> headers) {
		this(method, url, null, null);
	}
	
	public ScheduledInstanceHttp(String method, String url, byte[] body) {
		this(method, url, null, body);
	}
	
	public ScheduledInstanceHttp(String method, String url, Map<String, Object> headers, byte[] body) {
		this.method = method.toUpperCase();
		this.url = url;
		this.headers = headers;
		this.body = body;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

}
