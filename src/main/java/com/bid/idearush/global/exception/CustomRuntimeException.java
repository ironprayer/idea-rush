package com.bid.idearush.global.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomRuntimeException extends RuntimeException {

    public abstract HttpStatus getHttpStatus();
    public abstract String getMsg();

}
