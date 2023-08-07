package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BidWriteErrorCode {
    BID_PRICE_LOWER_THAN_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "현재 경매가보다 높아야 합니다." ),
    BID_PRICE_OVER_THAN_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "현재 경매가보다 10% 초과하여 입찰할 수 없습니다."),
    BID_PRICE_LOWER_THAN_STARTING_PRICE(HttpStatus.BAD_REQUEST, "경매 시작가와 같거나 높아야 합니다."),
    BID_PRICE_OVER_THAN_STARTING_PRICE(HttpStatus.BAD_REQUEST,"경매 시작가보다 10% 초과하여 입찰할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String msg;
}
