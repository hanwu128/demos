package com.hw.blog.interceptor;

import com.hw.blog.service.UserService;
import com.hw.blog.util.TokenUtil;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Token验证拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("token interceptor pre handle");
        }

        //从前端请求url获取token
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            logger.warn("request parameters token is null");
            return false;
        }

        Map<String, Object> tokenMap = TokenUtil.valid(token);
        if (tokenMap == null || tokenMap.isEmpty()) {
            logger.error("request parameters token is null");
            return false;
        }

        int i = (int) tokenMap.get("Result");
        if (i == 1) {
            logger.error("token validate is failure");
            return false;
        }
        if (i == 2) {
            logger.error("token is overdue");
            response.sendRedirect("/timeout.html");
        }
        if (i == 0) {
            JSONObject jsonObject = (JSONObject) tokenMap.get("data");
            Long id = (Long) jsonObject.get("id");
            String name = (String) jsonObject.get("name");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}