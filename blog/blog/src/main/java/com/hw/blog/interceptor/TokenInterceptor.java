package com.hw.blog.interceptor;

import com.hw.blog.model.LoginUser;
import com.hw.blog.service.UserService;
import com.hw.blog.util.ThreadUtil;
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
import java.io.IOException;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (logger.isDebugEnabled()) {
            logger.debug("token interceptor pre handle");
        }

        try {
            //从前端请求url获取token
            String token = request.getParameter("token");
            if (StringUtils.isEmpty(token)) {
                logger.warn("request parameters token is null");
                response.sendRedirect("/tokennull.html");
            }

            Map<String, Object> tokenMap = TokenUtil.valid(token);
            if (tokenMap == null || tokenMap.isEmpty()) {
                logger.error("request parameters token is null");
                response.sendRedirect("/tokennull.html");
            }

            int i = (int) tokenMap.get("Result");
            if (i == 1) {
                logger.error("token validate is failure");
                response.sendRedirect("/tokenfailure.html");
            }
            if (i == 2) {
                logger.error("token is overdue");
                response.sendRedirect("/timeout.html");
            }
            if (i == 0) {
                JSONObject jsonObject = (JSONObject) tokenMap.get("data");
                Long id = (Long) jsonObject.get("id");
                String name = (String) jsonObject.get("name");
                ThreadUtil.set(new LoginUser(id, name, token));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendRedirect("/servererror.html");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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
