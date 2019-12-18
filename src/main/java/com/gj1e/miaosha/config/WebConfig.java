package com.gj1e.miaosha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @Author GJ1e
 * @Create 2019/12/18
 * @Time 11:30
 */
@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {
    @Autowired
    UserArgumentResolver userArgumentResolver;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }
}