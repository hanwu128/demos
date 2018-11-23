package com.hw.blog.conf;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 重写授权规则
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //定制请求的授权规则
        http.authorizeRequests().anyRequest().permitAll();
                //.antMatchers("/user/**").hasRole("VIP2");
        //开启自动配置的登陆功能，效果，如果没有权限就会来到登陆页面
        http.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/login.html").failureForwardUrl("/login.html").successForwardUrl("/index.html");
        //1、/login来到登录页
        //2、重定向到/login?error表示登陆失败
        //3、更多详细规则
        //4、默认post形式的/login代表处理登陆请求
        //5、一旦定制LoginPage，那么loginPage的post请求就是登陆

        //开启自动配置的注销功能
        http.logout().logoutSuccessUrl("/login.html");
        //1、访问/logout表示用户注销，清空session
        //2、注销成功会返回/login?logout页面

        //开启记住我功能
        http.rememberMe().rememberMeParameter("remeber");
        //登陆成功后，将cookie发送给浏览器，以后访问页面带上这个cookie，只要通过检查就可以免登录
        //点击注销会删除cookie

        //关闭csrf保护
        http.csrf().disable();

        //允许页面在同一域名下访问
        http.headers().frameOptions().sameOrigin();

    }

    /**
     * 重写认证规则
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("aaa").password(new BCryptPasswordEncoder().encode("123")).roles("VIP1")
                .and().withUser("bbb").password(new BCryptPasswordEncoder().encode("123")).roles("VIP2");
    }
}
