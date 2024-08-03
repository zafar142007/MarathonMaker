package com.eagersphinx.domain.exception;

public enum ErrorCode {

    M1("No such bib exists for this event", 400),
    M2("no event found", 400);

    private String msg;
    private int statusCode;
    ErrorCode(String s, int status) {
        msg = s;
        statusCode = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
