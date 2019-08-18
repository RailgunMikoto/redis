package com.wxy.redis.common;

import lombok.Getter;

@Getter
public enum ResultEnum {

    FAIL(0, "失败"),
    SUCCESS(200, "成功"),
    PARAM_ERROR(300, "参数错误");

    private Integer code;
    private String message;

    private ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
