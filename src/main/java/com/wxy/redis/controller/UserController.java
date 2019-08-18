package com.wxy.redis.controller;

import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.entity.User;
import com.wxy.redis.service.UserService;
import com.wxy.redis.util.CookieUtil;
import com.wxy.redis.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Api(value = "用户登陆接口", description = "用户相关操作")
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @ResponseBody
    @ApiOperation(value = "用户登陆", response = ResultResponse.class, httpMethod = "POST")
    @RequestMapping("login/{username}/{password}")
    public ResultResponse login(HttpServletRequest request, HttpServletResponse response,
                        @PathVariable("username") @ApiParam(value = "用户名", name = "username", required = true) String username,
                        @PathVariable("password") @ApiParam(value = "密码", name = "password", required = true) String password) {
        ResultResponse<User> resultResponse = userService.userLogin(username, password);
        // 将用户信息存入redis中
        String redisKey = request.getSession().getId();
        redisUtil.setex(redisKey, resultResponse.getData(), CookieUtil.LOGIN_REDIS_EXPIRE);
        // 将redis中存储用户信息的key写入cookie
        CookieUtil.writeCookies(response, CookieUtil.LOGIN_COOKIE_NAME, redisKey,
                CookieUtil.LOGIN_DOMAIN, CookieUtil.PATH, CookieUtil.LOIN_COOKIE_EXPIRE);
        return resultResponse;
    }
}
