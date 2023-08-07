package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserFindErrorCode {
    USER_EMPTY(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.")
    ;

    private final HttpStatus status;
    private final String msg;

}
