package com.bid.idearush.global.exception;

import com.bid.idearush.global.exception.errortype.FileWriteErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class FileWriteException extends HttpStatusCodeException {

    public FileWriteException(FileWriteErrorCode fileWriteErrorCode){
        super(fileWriteErrorCode.getMsg(), fileWriteErrorCode.getStatus(), "", null, null, null);
    }

}
