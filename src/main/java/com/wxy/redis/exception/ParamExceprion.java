package com.wxy.redis.exception;

public class ParamExceprion extends RuntimeException {

    public ParamExceprion() {
        super();
    }

    public ParamExceprion(String message) {
        super(message);
    }

    public ParamExceprion(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamExceprion(Throwable cause) {
        super(cause);
    }

    protected ParamExceprion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
