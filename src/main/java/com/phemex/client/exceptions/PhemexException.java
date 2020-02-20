package com.phemex.client.exceptions;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PhemexException extends RuntimeException {

    final public int code; // application level return code, 0 means ok, o/w error.
    final public String respBody;
    final public String msg;

    public PhemexException(String message, int code, String responseBody) {
        super(message);
        this.code = code;
        respBody = responseBody;
        this.msg = message;
    }

    public PhemexException(String message, int code, String responseBody, Throwable cause) {
        super(message, cause);
        this.code = code;
        respBody = responseBody;
        this.msg = message;
    }

}
