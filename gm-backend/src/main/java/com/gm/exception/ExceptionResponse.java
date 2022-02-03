package com.gm.exception;

/**
 * exception message response
 * */
public class ExceptionResponse {

    private Integer code;

    private String message;

    public ExceptionResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}