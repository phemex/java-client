package com.phemex.client.message;

public class PhemexResponse<T> {

    public int code;

    private String msg;

    private T data;

    public String requestTraceId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestTraceId() {
        return requestTraceId;
    }

    public void setRequestTraceId(String requestTraceId) {
        this.requestTraceId = requestTraceId;
    }

    @Override
    public String toString() {
        return "PhemexResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", requestTraceId='" + requestTraceId + '\'' +
                '}';
    }
}
