package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserFindErrorCode {

    USER_EMPTY(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String msg;

}
