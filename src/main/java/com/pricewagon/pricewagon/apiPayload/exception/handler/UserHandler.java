package com.pricewagon.pricewagon.apiPayload.exception.handler;

import com.pricewagon.pricewagon.apiPayload.code.BaseErrorCode;
import com.pricewagon.pricewagon.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler (BaseErrorCode errorCode){
        super(errorCode);
    }
}
