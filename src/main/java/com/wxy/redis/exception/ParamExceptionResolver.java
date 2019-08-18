package com.wxy.redis.exception;

import com.wxy.redis.common.ResultResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ParamExceptionResolver {

    @ResponseBody
    @ExceptionHandler(value = ParamExceprion.class)
    public ResultResponse paramExceptionResolver(Exception e) {

        return ResultResponse.fail(e.getMessage());

    }

}
