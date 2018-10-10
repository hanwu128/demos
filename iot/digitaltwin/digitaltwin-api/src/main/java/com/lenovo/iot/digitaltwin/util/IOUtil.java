package com.lenovo.iot.digitaltwin.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Desc: IO工具类
 * @Name: com.lenovo.iot.digitaltwin.util.IOUtil
 * @Author: chench9@lenovo.com
 * @Date: 2018/7/12
 */
public class IOUtil {

    /**
     * 关闭输入流InputStream
     *
     * @param is
     */
    public static void closeInputStream(InputStream is) {
        if (is == null) {
            return;
        }

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输CloseableHttpResponse
     *
     * @param httpResponse
     */
    public static void closeCloseableHttpResponse(CloseableHttpResponse httpResponse) {
        if (httpResponse == null) {
            return;
        }

        try {
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输CloseableHttpClient
     *
     * @param httpClient
     */
    public static void closeCloseableHttpClient(CloseableHttpClient httpClient) {
        if (httpClient == null) {
            return;
        }

        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IOUtil() {
    }
}
