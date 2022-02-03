package com.gm.exception;

/**
 * service verify exception
 * */
public class ServiceVerifyException extends RuntimeException {

    private Integer code;

    private String message;

    public ServiceVerifyException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
