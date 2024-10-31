package com.pricewagon.pricewagon.apiPayload.exception.handler;

import com.pricewagon.pricewagon.apiPayload.code.BaseErrorCode;
import com.pricewagon.pricewagon.apiPayload.exception.GeneralException;

public class AlarmHandler extends GeneralException {
    public AlarmHandler (BaseErrorCode errorCode){
        super(errorCode);
    }
}
