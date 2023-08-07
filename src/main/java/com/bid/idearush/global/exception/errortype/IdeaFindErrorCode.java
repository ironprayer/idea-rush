package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IdeaFindErrorCode {

    KEYWORD_CATEGORY_SAME(HttpStatus.BAD_REQUEST, "전체조회 일때는 keyword 와 category 가 동시에 들어올수 없습니다."),
    IDEA_EMPTY(HttpStatus.BAD_REQUEST, "아이디어 게시글이 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String msg;

}
