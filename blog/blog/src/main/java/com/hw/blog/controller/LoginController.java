package com.hw.blog.controller;

import com.hw.blog.model.LoginUser;
import com.hw.blog.model.User;
import com.hw.blog.service.MessagesService;
import com.hw.blog.service.UserService;
import com.hw.blog.util.JsonResp;
import com.hw.blog.util.ThreadUtil;
import com.hw.blog.util.TokenUtil;
import com.nimbusds.jose.JOSEException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @Resource
    private MessagesService messagesService;

    /**
     * 主页
     *
     * @return
     */
    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    /**
     * 跳转到登陆
     *
     * @return
     */
    @RequestMapping(value = {"/", "/menu/user/login.html"})
    public String userLogin() {
        return "user/login";
    }

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("/login.html")
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, @RequestParam String password, @RequestParam String vercode) {
        try {

            //使用Shiro编写认证操作
            //1、获取Subject
            Subject subject = SecurityUtils.getSubject();

            //2、封装用户数据
            UsernamePasswordToken uoken = new UsernamePasswordToken(name, password);

            try {
                //3、执行登陆
                subject.login(uoken);

                //没有异常验证成功
                User user = userService.login(name, password);
                Map<String, Object> userMap = new HashMap<String, Object>();
                userMap.put("id", user.getId());
                userMap.put("name", user.getName());
                //生成时间
                userMap.put("sta", new Date().getTime());
                //过期时间，默认30分钟过期
                userMap.put("exp", new Date().getTime() + 30 * 60 * 1000);
                String token = TokenUtil.creatToken(userMap);

                ThreadUtil.set(new LoginUser(user.getId(), user.getName(), token));

                Long id = ThreadUtil.get().getId();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("access_token", token);

                return new JsonResp(map);
            } catch (UnknownAccountException e) {
                logger.error("用户名不存在！");
            } catch (IncorrectCredentialsException e) {
                logger.error("密码错误！");
            }

        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return JsonResp.httpCode(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR).errorResp(messagesService.getMessage("error", new Object[]{"login is error "}));
    }

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping("/register.html")
    public String loginPage() {
        return "admin/register/register";
    }

}
