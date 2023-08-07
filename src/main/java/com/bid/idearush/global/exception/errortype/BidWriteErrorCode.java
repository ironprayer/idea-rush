package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BidWriteErrorCode {
    INVALID_BID_PRICE(HttpStatus.BAD_REQUEST, "유효한 입찰 금액이 아닙니다." )
    ;

    private final HttpStatus status;
    private final String msg;
}
