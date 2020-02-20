package com.phemex.client;

import com.phemex.client.impl.PhemexClientBuilder;
import com.phemex.client.rest.Account;
import com.phemex.client.rest.Order;
import com.phemex.client.ws.PhemexMessageListener;
import com.phemex.client.ws.PhemexWebSocketClient;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.Executor;

public interface PhemexClient {

    Order orders();

    Account account();

    PhemexWebSocketClient createWebSocketClient();

    String apiKeySignature(long expiry);

    String apiKey();

    @Deprecated
    default String accessTokenSignature(long expiry) {
        return apiKeySignature(expiry);
    }

    @Deprecated
    default String accessToken() {
        return apiKey();
    }

    static Builder builder() {
        return new PhemexClientBuilder();
    }

    interface Builder {

        @Deprecated
        default Builder accessToken(String accessToken) {
            return this.apiKey(accessToken);
        }

        @Deprecated
        default Builder secretKey(String secretKey) {
            return this.apiSecret(secretKey);
        }

        Builder apiKey(String apikey);

        Builder apiSecret(String apiSecret);

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
