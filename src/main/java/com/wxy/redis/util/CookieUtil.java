package com.wxy.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    // 一级域名
    public static final String LOGIN_DOMAIN = "wxy.com";
    // redis登陆信息对应的key在cookie中的key名
    public static final String LOGIN_COOKIE_NAME = "login_token";
    // redis的用户过期时间 30分钟
    public static final int LOGIN_REDIS_EXPIRE = 60 * 30;
    // cookie的过期事件 24小时
    public static final int LOIN_COOKIE_EXPIRE = 60 * 60 * 24;
    // 可以获取此cookie的路径 / 表示所有路径
    public static final String PATH = "/";

    /**
     * 写入cookie
     * @param response     写cookie的工具
     * @param cookieKey    cookie的键
     * @param cookieValue  cookie的值
     * @param domain  cookie的一级域名
     * @param path         可以获取cookie的路径
     * @param expire cookie的过期时间
     */
    public static void writeCookies(HttpServletResponse response, String cookieKey, String cookieValue, String domain, String path, int expire) {

        Cookie cookie = new Cookie(cookieKey, cookieValue);
        // 设置域名
        cookie.setDomain(domain);
        // 设置路径
        cookie.setPath(path);
        // 设置过期时间
        cookie.setMaxAge(expire);
        // 设置js脚本无法读取到cookie信息
        cookie.setHttpOnly(true);
        log.info("CookieUtil: 写入cookie, writeCookies -> {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     * 获取cookie里面存储的值
     * @param request
     * @param cookieKey
     * @return
     */
    public static String readCookie(HttpServletRequest request, String cookieKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (StringUtils.equalsAnyIgnoreCase(cookieKey, cookie.getName())){
                    log.info("CookieUtil: readCookie 从cookie中获取了 -> {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie，
     * 重新添加一次cookie并设置cookie的过期时间为0
     * @param request
     * @param cookieKey
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (StringUtils.equalsAnyIgnoreCase(cookieKey, cookie.getName())){
                    log.info("CookieUtil: deleteCookie 删除了cookie -> {}", cookie.getName());
                    cookie.setMaxAge(0);
                    // 设置根路径
                    cookie.setPath(CookieUtil.PATH);
                    response.addCookie(cookie);
                    return ;
                }
            }
        }
        return ;
    }
}
