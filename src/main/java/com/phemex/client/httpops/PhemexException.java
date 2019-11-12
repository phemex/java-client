package com.phemex.client.httpops;

public class PhemexException extends RuntimeException {
    final public int statusCode;
    final public String respBody;

    public PhemexException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        respBody = responseBody;
    }

    public PhemexException(String message, Throwable cause) {
        super(message, cause);
        statusCode = 0;
        respBody = "";
    }

    public PhemexException(String message) {
        super(message);
        statusCode = 0;
        respBody = "";
    }
}
