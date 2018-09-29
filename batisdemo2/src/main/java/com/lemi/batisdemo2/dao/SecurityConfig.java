package com.lemi.batisdemo2.dao;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启security的注解

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        return new MyUserDetails();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf() //跨站
                .disable() //关闭跨站检测
                .authorizeRequests() //验证策略
                .anyRequest()       //所有请求
                .authenticated()    //必须验证
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")//自定义登录页
                .permitAll()
                .defaultSuccessUrl("/welcome",true) //登录成功后跳转页
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/myerror");//无权访问

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth

                .userDetailsService(customUserService())
                .passwordEncoder(new MyPasswordEncoder());


    }
}
