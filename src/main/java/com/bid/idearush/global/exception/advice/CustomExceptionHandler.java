package com.bid.idearush.global.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j(topic = "에러 처리")
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity errorHandler(HttpStatusCodeException e){
        log.error(e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).build();
    }

}
