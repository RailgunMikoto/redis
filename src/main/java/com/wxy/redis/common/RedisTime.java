package com.wxy.redis.common;

import lombok.Getter;

@Getter
public enum RedisTime {

    HALF_HOUR(30 * 30, "半小时");

    private Integer time;
    private String describe;

    RedisTime(Integer time, String describe) {
        this.time = time;
        this.describe = describe;
    }
}
