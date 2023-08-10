package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserFindErrorCode {

    USER_EMPTY(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다."),
    USER_ACCOUNT_ID_EMPTY(HttpStatus.BAD_REQUEST, "유저 아이디가 존재하지 않습니다."),
    USER_PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다.")
    ;

    private final HttpStatus status;
    private final String msg;

}
