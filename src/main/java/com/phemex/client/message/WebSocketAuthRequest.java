package com.phemex.client.message;

/**
 * Note: requestExpiry is the Epoc, its should be within 5 minutes from now.
 */
public class WebSocketAuthRequest {
    private String accessToken;
    private long requestExpiry;
    private String requestSignature;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getRequestExpiry() {
        return requestExpiry;
    }

    public void setRequestExpiry(long requestExpiry) {
        this.requestExpiry = requestExpiry;
    }

    public String getRequestSignature() {
        return requestSignature;
    }

    public void setRequestSignature(String requestSignature) {
        this.requestSignature = requestSignature;
    }
}
