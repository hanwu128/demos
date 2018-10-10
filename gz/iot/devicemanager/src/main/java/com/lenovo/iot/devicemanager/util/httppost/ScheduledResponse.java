/**
 * 
 */
package com.lenovo.iot.devicemanager.util.httppost;

/**
 * 调度响应
 * @desc com.lenovo.opinion.taskmgr.task.ScheduledResponse
 * @author chench9@lenovo.com
 * @date 2016年12月20日
 */
public abstract class ScheduledResponse {
	public static int CODE_OK = 200; // OK
	protected int responseCode = 0; //OK
	
	public ScheduledResponse(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
