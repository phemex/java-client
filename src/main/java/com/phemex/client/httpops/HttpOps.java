package com.phemex.client.httpops;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface HttpOps {
    CompletableFuture<String> sendAsync(URI uri, String accessToken, byte[] secretKey,
                                        String method, String queryString, long expiry, String body, Duration timeout);
}
