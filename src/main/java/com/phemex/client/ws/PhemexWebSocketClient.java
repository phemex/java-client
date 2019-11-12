package com.phemex.client.ws;


import com.phemex.client.PhemexClient;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class PhemexWebSocketClient extends WebSocketClient {

    static final Logger logger = LoggerFactory.getLogger(PhemexWebSocketClient.class);
    static AtomicLong idGenerator = new AtomicLong();

    private PhemexMessageListener listener;
    private PhemexClient client;

    public PhemexWebSocketClient(PhemexClient client, String wsUri, PhemexMessageListener listener) {
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
        System.out.println("Closed: " + code + " " + reason);
    }

    public void login() {
        long expiry = Instant.now().plusSeconds(120).getEpochSecond();
        String apiAuth = "{\"method\": \"user.auth\", \"params\": [\"API\", \"" +
                client.accessToken() + "\", \"" + client.accessTokenSignature(expiry) + "\", " + expiry + "], \"id\": 1234}";
        this.send(apiAuth);
    }

    public void subscribeAccountDetails() {
        this.send("{\"id\":" + idGenerator.incrementAndGet() + ", \"method\": \"aop.subscribe\", \"params\": []}");
    }

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