package com.wxy.redis.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserInfoEnum {

    USERNAME_PASSWORD_EMPTY(500, "用户名或密码不能为空"),
    USERNAME_PASSWORD_ERROR(500, "用户名或密码错误");

    private Integer code;
    private String message;

}
