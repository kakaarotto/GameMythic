package com.gm.dto;

/**
 * Response content transfer object
 * @author pujie
 */
public class ResponseDTO {

    private Integer code;

    private String message;

    public ResponseDTO(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public ResponseDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public enum ExceptionEnum {

        // System exception
        USER_NOT_LOGIN(-9, "User is not logged in");


        private Integer code;

        private String message;

        ExceptionEnum(ExceptionEnum exceptionEnum) {
            this.code = exceptionEnum.getCode();
            this.message = exceptionEnum.getMessage();
        }

        ExceptionEnum(Integer code, String message) {
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
}

