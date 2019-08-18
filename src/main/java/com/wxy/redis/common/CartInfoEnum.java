package com.wxy.redis.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartInfoEnum {

    IS_EMPTY(250, "购物车空空如也"),
    IS_CHECKED(1, "选中商品"),
    NOT_CHECKD(0, "没有选中商品"),
    QUANTITY(1222, "QUANTITY"),
    CHECKED(1222, "CHECKED");

    private Integer code;
    private String message;

}
