/**
 * 
 */
package com.lenovo.iot.devicemanager.util.httppost;

/**
 * 调度响应http实现
 * 
 * @desc com.lenovo.opinion.taskmgr.task.ScheduledResponseHttp
 * @author chench9@lenovo.com
 * @date 2016年12月20日
 */
public class ScheduledResponseHttp extends ScheduledResponse {
	private String responseMessage = ""; // 响应消息,如: HTTP/1.0 200 OK
	private String contentType = ""; // 响应内容类型
	private String contentEncoding = ""; // 响应内容编码
	private int contentLength = 0; // 响应内容大小
	private byte[] content = null; // 响应内容

	public ScheduledResponseHttp(int responseCode) {
		super(responseCode);
	}

	public ScheduledResponseHttp(int responseCode, byte[] content) {
		super(responseCode);
		this.content = content;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
