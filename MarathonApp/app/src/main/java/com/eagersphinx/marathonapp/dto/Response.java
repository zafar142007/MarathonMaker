package com.eagersphinx.marathonapp.dto;

public class Response {
    protected long timestamp = System.currentTimeMillis();

    protected String errorMessage;

    public Response(long timestamp, String errorMessage) {
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
    }

    public Response() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}