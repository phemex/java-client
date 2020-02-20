package com.phemex.client.impl;

import com.phemex.client.PhemexClient;
import com.phemex.client.PhemexClient.Builder;
import com.phemex.client.httpops.ApiState;
import com.phemex.client.httpops.HttpOps;
import com.phemex.client.httpops.HttpOpsBuilder;
import com.phemex.client.ws.PhemexMessageListener;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhemexClientBuilder implements PhemexClient.Builder {

    String apiKey;
    String url;
    String wsUri;
    byte[] apiSecret;
    Clock clock = Clock.systemUTC();
    Duration connectionTimeout = Duration.ofSeconds(60);
    Duration expiryDuration = Duration.ofSeconds(10);
    PhemexMessageListener messageListener;

    Executor executor;

    @Override
    public Builder apiKey(String apikey) {
        this.apiKey = Objects.requireNonNull(apikey);
        return this;
    }

    @Override
    public Builder apiSecret(String apiSecret) {
        this.apiSecret = Objects.requireNonNull(apiSecret).getBytes(StandardCharsets.UTF_8);
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
        Objects.requireNonNull(this.apiKey);
        Objects.requireNonNull(this.messageListener);
        Objects.requireNonNull(this.wsUri);
        Objects.requireNonNull(this.apiSecret);

        String apiKey = this.apiKey;
        String url = Objects.requireNonNull(this.url);
        if (this.executor == null) {
            this.executor = Executors.newSingleThreadExecutor();
        }
        Executor executor = this.executor;

        HttpOps httpOps = HttpOpsBuilder.newBuilder()
            .connectionTimeout(connectionTimeout)
            .executor(executor)
            .build();

        ApiState s = new ApiState(httpOps, url, apiKey, clock, expiryDuration.toMillis(), this.apiSecret);
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
            public PhemexWebSocketClientImpl createWebSocketClient() {
                return new PhemexWebSocketClientImpl(this, wsUri, messageListener);
            }

            @Override
            public String apiKeySignature(long expiry) {
                return ClientUtils.signAccessToken(apiKey, expiry, apiSecret);
            }

            @Override
            public String apiKey() {
                return apiKey;
            }

        };
    }
}