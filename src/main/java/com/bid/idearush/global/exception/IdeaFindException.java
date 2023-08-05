package com.bid.idearush.global.exception;

import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class IdeaFindException extends HttpStatusCodeException {

    public IdeaFindException(IdeaFindErrorCode ideaFindErrorCode){
        super(ideaFindErrorCode.getMsg(), ideaFindErrorCode.getStatus(), "", null, null, null);
    }

}
