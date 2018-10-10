package com.lenovo.iot.digitaltwin;

import java.net.URI;

/**
 * @Desc: //TODO
 * @Name: com.lenovo.iot.digitaltwin.URLTest
 * @Author: chench9@lenovo.com
 * @Date: 2018/9/6
 */
public class URLTest {

    public static void main(String[] args) {
        // URL中不能包含特殊字符
        String url = "http://localhost:8400/dt/open/v1/instances?name=%";
        URI.create(url);
    }
}
