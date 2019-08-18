package com.wxy.redis.common;

import lombok.Getter;

@Getter
public enum ProductInfoEnum {

    NOT_EXISTS(100, "商品不存在"),
    STOCK_SHORTAGE(100, "商品库存不足");

    private Integer code;
    private String message;

    private ProductInfoEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
