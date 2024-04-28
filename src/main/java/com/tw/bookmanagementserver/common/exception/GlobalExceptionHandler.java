package com.tw.bookmanagementserver.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResult handle(Exception e) {
        log.error("Exception:" + e.getMessage());
        return new ErrorResult(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult businessException(BusinessException e) {
        log.error("Business Exception:" + e.getMessage());
        return new ErrorResult(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult runtimeException(RuntimeException e) {
        log.error("Runtime Exception:" + e.getMessage());
        return new ErrorResult(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }
}
