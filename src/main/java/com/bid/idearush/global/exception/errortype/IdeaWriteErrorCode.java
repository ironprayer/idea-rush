package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IdeaWriteErrorCode {

    IDEA_UNAUTH(HttpStatus.BAD_REQUEST, "아이디어에 권한이 없습니다.")
    ;

    private final HttpStatus status;
    private final String msg;

}