package com.eagersphinx.domain.exception;

public class MarathonException extends RuntimeException {

    private ErrorCode errorCode;
    public MarathonException(ErrorCode errorCode, Exception parent) {
        super(errorCode.getMsg(), parent);
        this.errorCode = errorCode;
    }
    public MarathonException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
