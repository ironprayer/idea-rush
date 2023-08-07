package com.bid.idearush.global.exception;

import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class IdeaFindExceptionCustom extends ErrorCustomRuntimeException {

    private final IdeaFindErrorCode ideaFindErrorCode;

    @Override
    public HttpStatus getHttpStatus() {
        return ideaFindErrorCode.getStatus();
    }

    @Override
    public String getMsg() {
        return ideaFindErrorCode.getMsg();
    }

}
