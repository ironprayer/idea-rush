package com.bid.idearush.global.exception;

import com.bid.idearush.global.exception.errortype.BidWriteErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class BidWriteException extends HttpStatusCodeException {

    public BidWriteException(BidWriteErrorCode bidWriteErrorCode){
        super(bidWriteErrorCode.getMsg(), bidWriteErrorCode.getStatus(), "", null, null, null);
    }

}
