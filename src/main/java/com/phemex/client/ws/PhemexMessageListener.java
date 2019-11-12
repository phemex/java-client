package com.phemex.client.ws;

public interface PhemexMessageListener {

    void onMessage(String json);
    void onError(Exception ex);
}
