package com.hw.jwt.config;

import com.hw.jwt.filter.JwtAuthenticationFilter;
import com.hw.jwt.filter.JwtLoginFilter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtLoginFilter(authenticationManager()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()));
    }
}
