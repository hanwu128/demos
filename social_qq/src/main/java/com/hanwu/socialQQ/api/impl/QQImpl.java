package com.hanwu.socialQQ.api.impl;

import com.hanwu.socialQQ.api.QQ;
import com.hanwu.socialQQ.api.QQUserInfo;
import org.codehaus.jackson.map.ObjectMapper;

@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    //http://wiki.connect.qq.com/openapi%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E_oauth2-0
    private static final String QQ_URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    //http://wiki.connect.qq.com/get_user_info(access_token由父类提供)
    private static final String QQ_URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
    //appId 配置文件读取
    private String appId;
    //openId 请求QQ_URL_GET_OPENID返回
    private String openId;
    //工具类
    private ObjectMapper objectMapper = new ObjectMapper();

    //构造方法获取openId
    public QQImpl(String accessToken, String appId) {
        super(accessToken,TokenStrategy);
        this.appId = appId;
        String url = String.format(QQ_URL_GET_OPENID,accessToken);
        String result = getRestTemplate
    }


    @Override
    public QQUserInfo getUserInfo() {
        return null;
    }
}
