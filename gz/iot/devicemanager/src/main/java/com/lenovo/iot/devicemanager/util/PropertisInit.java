package com.lenovo.iot.devicemanager.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by root on 2017/2/7.
 */

@Configuration
public class PropertisInit {

   // @Value("#{idcommonproperties.commonspiderurl}")
//    public  String url;

    //@Value("${commonspiderurl}")
//    public  String port;

//    public PropertisInit(@Value("${commonspiderurl}") String url,
//                              @Value("${commonspiderport}") String port
//    ){
//        this.url = url;
//
//        this.port = port;
//        System.out.println("url--->>>"+url);
//    }


    public PropertisInit(){


//        System.out.println("url222--->>>"+url);
//        url = @Value("${commonspiderurl}")


    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }




}
