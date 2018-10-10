package com.lenovo.iot.devicemanager.util;

public class OpenTSDBResult {

	private int status_code;
	private String response;
	
	public OpenTSDBResult() {
		this.status_code = 0;
		this.response = "";
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
