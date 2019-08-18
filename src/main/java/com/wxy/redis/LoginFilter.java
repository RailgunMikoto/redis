package com.wxy.redis;

import com.wxy.redis.util.CookieUtil;
import com.wxy.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private RedisUtil redisUtil;

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("登录过滤器开始拦截....");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 放行相关路径
        String uri = request.getRequestURI();
        log.info("当前访问路径为:{}",uri);
        if (uri.contains("login") || uri.contains("/favicon.ico") || uri.contains("swagger") || uri.contains("/api-docs")){
            filterChain.doFilter(request, response);
            return;
        }
        String value = CookieUtil.readCookie(request, CookieUtil.LOGIN_COOKIE_NAME);
        Cookie[] cookies = request.getCookies();
        if (value != null) {
            Object user = redisUtil.get(value);
            if (user != null) {
                // 刷新redis中用户信息的过期时间
                redisUtil.expire(value, CookieUtil.LOGIN_REDIS_EXPIRE);
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write("用户未登录...");
    }

    @Override
    public void destroy() {
        log.info("登录过滤器开始摧毁....");
    }
}
