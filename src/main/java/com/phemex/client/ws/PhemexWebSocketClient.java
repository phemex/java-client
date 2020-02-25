package com.phemex.client.ws;

import com.phemex.client.constant.KlineInterval;
import java.util.concurrent.TimeUnit;
import org.java_websocket.WebSocket;

public interface PhemexWebSocketClient extends WebSocket {

    boolean connectBlocking(long timeout, TimeUnit timeUnit) throws InterruptedException;

    void closeBlocking() throws InterruptedException;

    void connect();

    boolean connectBlocking() throws InterruptedException;

    void login();

    void subscribeAccountDetails();

    void subscribeTrades(String symbol);

    void subscribeKline(String symbol, KlineInterval interval);

    void subscribeTick(String symbol);

    void subscribeOrderBook(String... symbols);

}
