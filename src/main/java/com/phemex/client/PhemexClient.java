package com.phemex.client;

import com.phemex.client.impl.OrderImpl;
import com.phemex.client.impl.PhemexClientBuilder;
import com.phemex.client.impl.AccountImpl;
import com.phemex.client.ws.PhemexMessageListener;
import com.phemex.client.ws.PhemexWebSocketClient;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.Executor;

public interface PhemexClient {
    OrderImpl orders();

    AccountImpl account();

    PhemexWebSocketClient createWebSocketClient();

    String accessTokenSignature(long expiry);

    String accessToken();

    static Builder builder() {
        return new PhemexClientBuilder();
    }

    interface Builder {
        Builder accessToken(String accessToken);

        Builder secretKey(String secretKey);

        Builder url(String url);

        Builder wsUri(String wsUri);

        Builder messageListener(PhemexMessageListener messageListener);

        Builder executor(Executor executor);

        Builder clock(Clock c);

        Builder expiryDuration(Duration expiryDuration);

        Builder connectionTimeout(Duration connectionTimeout);

        PhemexClient build();
    }

}
