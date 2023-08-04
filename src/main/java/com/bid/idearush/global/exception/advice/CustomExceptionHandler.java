package com.bid.idearush.global.exception.advice;

import com.bid.idearush.global.exception.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "에러 처리")
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity errorHandler(CustomRuntimeException e){
        log.error(e.getMsg());
        return ResponseEntity.status(e.getHttpStatus()).build();
    }

}
