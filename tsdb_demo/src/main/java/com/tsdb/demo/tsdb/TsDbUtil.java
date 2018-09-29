package com.tsdb.demo.tsdb;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Desc: TSDB工具类
 * Name: com.lenovo.iot.digitaltwin.util.TsDbUtil
 * Author: hanwu
 * Date: 2018/7/6 11:12
 **/
public class TsDbUtil {
    private static final Logger logger = LoggerFactory.getLogger(TsDbUtil.class);

    /**
     * 查询数据
     * @param url
     * @return
     */
    public static String query(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseBody = null;
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());

            // 创建一个自定义响应处理程序
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(
                        final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        logger.warn("Not found in opentsdb!");
                        throw new IOException("Unexpected response status: " + status);
                    }
                }
            };
            responseBody = client.execute(httpget, responseHandler);
        } catch (Exception e) {
            logger.error("failed to create opentsdb connection:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("close to create opentsdb connection:{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return responseBody;
    }


}
