package com.wxy.redis.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> {

    private Integer code;
    private String message;

    // 返回json时，忽略data为null的属性
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 不返回数据也不携带信息的方法
    public ResultResponse (Integer code, String message){
        this.code = code;
        this.message = message;
    }

    // 失败，不返回数据，携带信息
    public static ResultResponse fail(String message){
        return new ResultResponse(ResultEnum.FAIL.getCode(), message);
    }

    // 失败，不返回数据，不携带信息
    public static ResultResponse fail(){
        return new ResultResponse(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
    }

    // 失败，返回数据，携带信息
    public static <T>ResultResponse fail(String message, T data){
        return new ResultResponse(ResultEnum.FAIL.getCode(), message, data);
    }

    // 失败，返回数据，不携带信息
    public static <T>ResultResponse fail(T data){
        return new ResultResponse(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage(), data);
    }

    // 成功，返回数据
    public static <T>ResultResponse success(T data){
        return new ResultResponse<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    // 成功，不返回数据
    public static ResultResponse success(){
        return new ResultResponse(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

}
