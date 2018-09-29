package com.tsdb.demo.tsdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Value("${tsdb.url}")
    private String tsDbUrl;

    @Value("${tsdb.port}")
    public String tsDbPort;

    public String getTsDbUrl() {
        return tsDbUrl;
    }

    public void setTsDbUrl(String tsDbUrl) {
        this.tsDbUrl = tsDbUrl;
    }

    public String getTsDbPort() {
        return tsDbPort;
    }

    public void setTsDbPort(String tsDbPort) {
        this.tsDbPort = tsDbPort;
    }
}
