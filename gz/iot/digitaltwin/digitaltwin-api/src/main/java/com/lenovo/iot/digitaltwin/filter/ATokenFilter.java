package com.lenovo.iot.digitaltwin.filter;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.util.*;
import com.lenovo.iot.digitaltwin.model.ThreadUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Desc: 获取前端传的Token
 *       Filter默认根据文件名排序,本程序Filter按照英文字母顺序进行排序,例如TokenFilter先执行,在TokenFilter前面加上A
 * Name: com.lenovo.iot.digitaltwin.filter.TokenFilter
 * Author: hanwu
 * Date: 2018/8/17 11:00
 **/
//@WebFilter(urlPatterns = {"*.ft"})
public class ATokenFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ATokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("token filter init");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("token filter do filter");
        }
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //从前端请求url获取token
        String token = req.getParameter("token");
        if (token == null || "".equals(token)) {
            logger.warn("token is null");
            chain.doFilter(request, response);
            return;
        }

        JsonResp str = JsonResp.getErrorResp(HttpServletResponse.SC_BAD_REQUEST, JsonResp.MSG_PARAM_ERROR);
        String results = TokenUtil.volidate(token);
        if (StringUtils.isEmpty(results)) {
            logger.info("token is invalid:{}", token);
            ResponseUtil.write(resp,CommonUtil.obj2JSON(str));
            return;
        }

        JSONObject object = CommonUtil.str2JSONObject(results);
        String errormsg = object.getString("errormsg");
        if (!StringUtils.isEmpty(errormsg)) {
            logger.info("token is invalid:{},{}", errormsg, token);
            ResponseUtil.write(resp,CommonUtil.obj2JSON(str));
            return;
        }

        //获取用户信息
        String username = object.getString("username");
        String userId = object.getString("userId");
        String groupName = object.getString("groupName");
        String groupId = object.getString("groupId");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(userId) ||
                StringUtils.isEmpty(groupName) || StringUtils.isEmpty(groupId)) {
            logger.info("token is invalid:{}", token);
            ResponseUtil.write(resp,CommonUtil.obj2JSON(str));
            return;
        }

        //把用户信息添加到ThreadLocal中
        ThreadUser user = new ThreadUser();
        user.setUsername(username);
        user.setUserId(userId);
        user.setGroupName(groupName);
        user.setGroupId(groupId);
        ThreadUtil.getThreadInstance().set(user);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("token filter destroy");
        }
    }
}
