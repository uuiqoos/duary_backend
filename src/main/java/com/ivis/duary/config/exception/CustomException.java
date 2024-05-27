package com.ivis.duary.config.exception;

import com.ivis.duary.config.response.ResponseCode;

public class CustomException extends RuntimeException {

    ResponseCode errorCode;

    public CustomException(String message) {
        super(message);
        errorCode = ResponseCode.INTERNAL_SERVER_ERROR;
    }
    public CustomException(ResponseCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(ResponseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ResponseCode getErrorCode() {
        return errorCode;
    }
}
