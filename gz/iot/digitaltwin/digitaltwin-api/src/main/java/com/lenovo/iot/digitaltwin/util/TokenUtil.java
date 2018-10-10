package com.lenovo.iot.digitaltwin.util;

import com.lenovo.iot.digitaltwin.config.Config;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Desc: Token工具类
 * Name: com.lenovo.iot.digitaltwin.filter.TokenUtil
 * Author: hanwu
 * Date: 2018/8/17 11:00
 **/
@Component
public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    @Autowired
    private Config config;

    private static TokenUtil tokenUtil;

    @PostConstruct
    public void init() {
        tokenUtil = this;
    }

    /**
     * 校验Token
     *
     * @param token
     * @return
     */
    public static String volidate(String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseBody = null;
        try {
            HttpGet httpget = new HttpGet(tokenUtil.config.getTokenUrl() + token);
            httpget.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            // 创建一个自定义响应处理程序
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(
                        final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (HttpServletResponse.SC_OK == status) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        logger.warn("token is invalid:{}",token);
                        return null;
                    }
                }
            };
            responseBody = client.execute(httpget, responseHandler);
        } catch (Exception e) {
            logger.error("failed to create token volidate connection:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("close to create token volidate connection:{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return responseBody;
    }
}
