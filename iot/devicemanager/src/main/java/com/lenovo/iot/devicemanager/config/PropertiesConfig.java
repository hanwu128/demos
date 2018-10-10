package com.lenovo.iot.devicemanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

// 配置信息类，可以同时加载多个配置信息
@Configuration
@PropertySources({
        @PropertySource("classpath:/tsdb.properties"),
        @PropertySource("classpath:/email.properties")
})
public class PropertiesConfig {
    @Autowired
    private Environment env;

    @Bean
    public TsDb tsDb() {
        TsDb tsDb = new TsDb();
        tsDb.setHost(env.getProperty("host"));
        tsDb.setPort(env.getProperty("port"));
        tsDb.setApiQueryMetric(env.getProperty("api_query_metric"));
        tsDb.setApiSuggest(env.getProperty("api_query_suggest"));
        return tsDb;
    }
}
