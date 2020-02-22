package com.phemex.client.message;

import lombok.Data;

@Data
public class PhemexResponse<T> {

    public int code;

    private String msg;

    private T data;

}
