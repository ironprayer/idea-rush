package com.bid.idearush.global.exception;

import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class UserFindException extends HttpStatusCodeException {

    public UserFindException(UserFindErrorCode userFindErrorCode){
        super(userFindErrorCode.getMsg(), userFindErrorCode.getStatus(), "", null, null, null);
    }

}