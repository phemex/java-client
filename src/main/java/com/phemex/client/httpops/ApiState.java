package com.phemex.client.httpops;

import java.time.Clock;

public class ApiState {
    public final HttpOps httpOps;
    public final String url;
    public final byte[] apiSecret;
    public final String apiKey;
    public final Clock clock;
    public final long expiryMillis;

    public ApiState(HttpOps httpOps, String url, String apiKey, Clock clock, long expiryMillis, byte[] apiSecret) {
        this.httpOps = httpOps;
        this.url = url;
        this.apiKey = apiKey;
        this.clock = clock;
        this.expiryMillis = expiryMillis;
        this.apiSecret = apiSecret;
    }
}
