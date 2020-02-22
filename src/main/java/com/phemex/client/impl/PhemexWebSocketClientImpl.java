package com.phemex.client.impl;


import com.phemex.client.PhemexClient;
import com.phemex.client.constant.KlineInterval;
import com.phemex.client.ws.PhemexMessageListener;
import com.phemex.client.ws.PhemexWebSocketClient;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class PhemexWebSocketClientImpl extends WebSocketClient implements PhemexWebSocketClient {

    static final Logger logger = LoggerFactory.getLogger(PhemexWebSocketClientImpl.class);
    static AtomicLong idGenerator = new AtomicLong();

    private PhemexMessageListener listener;
    private PhemexClient client;

    public PhemexWebSocketClientImpl(PhemexClient client, String wsUri, PhemexMessageListener listener) {
        super(URI.create(wsUri), new Draft_6455());
        this.client = client;
        this.listener = listener;
    }

    @Override
    public void onMessage(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug("Receive Message：" + message);
        }
        this.listener.onMessage(message);
    }

    @Override
    public void onMessage(ByteBuffer blob) {
        String json = new String(blob.array());
        if (logger.isDebugEnabled()) {
            logger.debug("Receive Message：" + json);
        }
        this.listener.onMessage(json);
    }

    @Override
    public void onError(Exception ex) {
        this.listener.onError(ex);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Closed: code {}, reason {}", code, reason);
    }

    @Override
    public void login() {
        long expiry = Instant.now().plusSeconds(120).getEpochSecond();
        String apiAuth = "{\"method\": \"user.auth\", \"params\": [\"API\", \"" +
            client.apiKey() + "\", \"" + client.apiKeySignature(expiry) + "\", " + expiry + "], \"id\": 1234}";
        this.send(apiAuth);
    }

    @Override
    public void subscribeAccountDetails() {
        this.send("{\"id\":" + idGenerator.incrementAndGet() + ", \"method\": \"aop.subscribe\", \"params\": []}");
    }

    @Override
    public void subscribeTrades(String symbol) {
        this.send("{\"id\":" + idGenerator.incrementAndGet() + ", \"method\": \"trade.subscribe\", \"params\":[\"" + Objects.requireNonNull(symbol) + "\"]}");
    }

    @Override
    public void subscribeKline(String symbol, KlineInterval interval) {
        String str = "{\"id\":" + idGenerator.incrementAndGet() + ", \"method\": \"kline.subscribe\", \"params\":[\"" + symbol + "\"," + interval.getInterval() + "]}";
        logger.debug("sub str {}", str);
        this.send(str);
    }

    @Override
    public void subscribeTick(String symbol) {
        this.send("{\"id\":" + idGenerator.incrementAndGet() + ", \"method\":\"tick.subscribe\"," + "\"params\": [\"" + Objects.requireNonNull(symbol) + "\"]}");
    }

    @Override
    public void subscribeOrderBook(String... symbols) {
        StringBuilder sb = new StringBuilder();
        for (String s : symbols) {
            sb.append("\"" + s + "\" ,");
        }
        sb.deleteCharAt(sb.length() - 1);
        String sym = sb.toString();
        this.send("{\"id\":" + idGenerator.incrementAndGet() + ", \"method\": \"orderbook.subscribe\", \"params\": [" + sym + "]}");
    }

}