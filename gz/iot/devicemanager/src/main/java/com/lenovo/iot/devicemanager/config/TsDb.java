package com.lenovo.iot.devicemanager.config;

public class TsDb {
    private String host = null;
    private String port = null;
    private String apiQueryMetric = null;
    private String apiSuggest = null;

	public String getUrlQueryMetric() {
        return new StringBuilder()
                .append(getUrlBase())
                .append(this.apiQueryMetric)
                .toString();
    }

    private String getUrlBase() {
        return new StringBuilder().append("http://")
                .append(this.host)
                .append(":")
                .append(this.port)
                .toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getApiQueryMetric() {
        return apiQueryMetric;
    }

    public void setApiQueryMetric(String apiQueryMetric) {
        this.apiQueryMetric = apiQueryMetric;
    }

    public String getApiSuggest() {
		return apiSuggest;
	}

	public void setApiSuggest(String apiSuggest) {
		this.apiSuggest = apiSuggest;
	}
}
