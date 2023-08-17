package com.bid.idearush.global.exception;

import com.bid.idearush.global.exception.errortype.IdeaWriteErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class IdeaWriteException extends HttpStatusCodeException {

    public IdeaWriteException(IdeaWriteErrorCode ideaWriteErrorCode){
        super(ideaWriteErrorCode.getMsg(), ideaWriteErrorCode.getStatus(), "", null, null, null);
    }

}
