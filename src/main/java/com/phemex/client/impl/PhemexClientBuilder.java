package com.phemex.client.impl;

import com.phemex.client.PhemexClient;
import com.phemex.client.httpops.ApiState;
import com.phemex.client.httpops.HttpOps;
import com.phemex.client.httpops.HttpOpsBuilder;
import com.phemex.client.ws.PhemexMessageListener;
import com.phemex.client.ws.PhemexWebSocketClient;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhemexClientBuilder implements PhemexClient.Builder {
    String accessToken;
    String url;
    String wsUri;
    byte[] secretKey;
    Clock clock = Clock.systemUTC();
    Duration connectionTimeout = Duration.ofSeconds(60);
    Duration expiryDuration = Duration.ofSeconds(10);
    PhemexMessageListener messageListener;

    Executor executor;

    @Override
    public PhemexClient.Builder accessToken(String accessToken) {
        this.accessToken = Objects.requireNonNull(accessToken);
        return this;
    }

    @Override
    public PhemexClient.Builder secretKey(String secretKey) {
        this.secretKey = Base64.getUrlDecoder().decode(secretKey);
        return this;
    }

    @Override
    public PhemexClient.Builder url(String url) {
        this.url = Objects.requireNonNull(url);
        return this;
    }

    @Override
    public PhemexClient.Builder wsUri(String wsUri) {
        this.wsUri = Objects.requireNonNull(wsUri);
        return this;
    }


    @Override
    public PhemexClient.Builder messageListener(PhemexMessageListener messageListener) {
        this.messageListener = Objects.requireNonNull(messageListener);
        return this;
    }

    @Override
    public PhemexClient.Builder executor(Executor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public PhemexClient.Builder clock(Clock c) {
        this.clock = Objects.requireNonNull(c);
        return this;
    }

    @Override
    public PhemexClient.Builder expiryDuration(Duration expiryDuration) {
        this.expiryDuration = Objects.requireNonNull(expiryDuration);
        return this;
    }


    @Override
    public PhemexClient.Builder connectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = Objects.requireNonNull(connectionTimeout);
        return this;
    }

    @Override
    public PhemexClient build() {
        Objects.requireNonNull(this.accessToken);
        Objects.requireNonNull(this.messageListener);
        Objects.requireNonNull(this.wsUri);
        Objects.requireNonNull(this.secretKey);

        String accessToken = this.accessToken;
        String url = Objects.requireNonNull(this.url);
        if (this.executor == null) {
            this.executor = Executors.newSingleThreadExecutor();
        }
        Executor executor = this.executor;

        HttpOps httpOps = HttpOpsBuilder.newBuilder()
                .connectionTimeout(connectionTimeout)
                .executor(executor)
                .build();

        ApiState s = new ApiState(httpOps, url, accessToken, clock, expiryDuration.toMillis() / 1000L, this.secretKey);
        OrderImpl orderClient = new OrderImpl(s);
        AccountImpl accountImpl = new AccountImpl(s);

        return new PhemexClient() {
            @Override
            public OrderImpl orders() {
                return orderClient;
            }

            @Override
            public AccountImpl account() {
                return accountImpl;
            }

            @Override
            public PhemexWebSocketClient createWebSocketClient() {
                return new PhemexWebSocketClient(this,  wsUri, messageListener);
            }

            @Override
            public String accessTokenSignature(long expiry) {
                return ClientUtils.signAccessToken(accessToken, expiry, secretKey);
            }

            @Override
            public String accessToken() {
                return accessToken;
            }

        };
    }
}