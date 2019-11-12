package com.phemex.client.httpops;

import javax.crypto.Cipher;
import java.time.Clock;

public class ApiState {
    public final HttpOps httpOps;
    public final String url;
    public final byte[] secretKey;
    public final String accessToken;
    public final Clock clock;
    public final long expiryMillis;

    public ApiState(HttpOps httpOps, String url, String accessToken, Clock clock, long expiryMillis, byte[] secretKey) {
        this.httpOps = httpOps;
        this.url = url;
        this.accessToken = accessToken;
        this.clock = clock;
        this.expiryMillis = expiryMillis;
        this.secretKey = secretKey;
    }
}
