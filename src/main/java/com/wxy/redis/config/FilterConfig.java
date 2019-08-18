package com.wxy.redis.config;

import com.google.common.collect.Lists;
import com.wxy.redis.LoginFilter;
import com.wxy.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {

    @Autowired
    private RedisUtil redisUtil;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        LoginFilter filter = new LoginFilter();
        filter.setRedisUtil(redisUtil);
        filterFilterRegistrationBean.setFilter(filter);
        // 设置拦截路径与优先级
        filterFilterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        // 正数越小优先级越高
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }
}
