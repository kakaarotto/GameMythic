package com.file.api.component.exception;

import com.file.api.dto.ResultEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseBody
  protected ResponseEntity<ResultEntity> handleException(RuntimeException ex, WebRequest request) {
    ex.printStackTrace();
    switch(ex.getClass().getName()){
      case "CoreException":
        return new ResponseEntity(new ResultEntity("01",ex.getMessage()), HttpStatus.OK);
      default:
        return new ResponseEntity(new ResultEntity("01","system error:" + ex.getMessage()), HttpStatus.OK);
    }
  }
}