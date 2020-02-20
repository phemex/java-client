package com.phemex.client.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phemex.client.httpops.ApiState;
import com.phemex.client.exceptions.PhemexException;
import com.phemex.client.message.PhemexResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
public final class ClientUtils {

    public static final ObjectMapper objectMapper = new ObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        .setSerializationInclusion(Include.NON_NULL);

    static URI newUriUnchecked(String baseUrl, String path) {
        try {
            return new URI(baseUrl + path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("invalid url");
        }
    }

    /**
     * HMacSha256( URL Path + QueryString + Expiry + body )
     */
    public static String sign(String path, String queryString, long expiry, String body, byte[] secretKey) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append(path)
            .append(queryString == null ? "" : queryString)
            .append(expiry)
            .append((body == null ? "" : body));
        String signedStr = sb.toString();
        log.debug("path {}, query str {}, expiry {}, body {}, signed str {}", path, queryString, expiry, body, signedStr);
        String signature = sign(signedStr, secretKey);
        return signature;
    }


    private static String sign(String message, byte[] secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            return new String(Hex.encodeHex(sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign message, error: " + e.getMessage(), e);
        }
    }

    public static String signAccessToken(String accessToken, long expiry, byte[] secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            return new String(Hex.encodeHex(sha256_HMAC.doFinal((accessToken + "" + expiry).getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign message, error: " + e.getMessage(), e);
        }
    }

    static String encodePath(String source) {

        byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
        boolean changed = false;
        for (byte b : bytes) {
            if (b < 0) {
                b += 256;
            }
            char c = (char) b;
            if (c == '/' || c == ':' || c == '@' || '!' == c || '$' == c || '&' == c || '\'' == c || '(' == c || ')' == c || '*' == c || '+' == c ||
                ',' == c || ';' == c || '=' == c || '-' == c || '.' == c || '_' == c || '~' == c || Character.isAlphabetic(c) || (c >= '0' && c <= '9')) {
                bos.write(b);
            } else {
                bos.write('%');
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                bos.write(hex1);
                bos.write(hex2);
                changed = true;
            }
        }
        return (changed ? new String(bos.toByteArray(), StandardCharsets.UTF_8) : source);
    }


    public static <T> CompletableFuture<PhemexResponse<T>> sendRequest(ApiState s, String path, String queryString, String method, String body,
        TypeReference<PhemexResponse<T>> typeReference) throws PhemexException {
        URI uri = newUriUnchecked(s.url, path);

        long expiry = s.clock.millis() + s.expiryMillis;
        log.debug("expiry is {}, expiry millis {}", expiry, s.expiryMillis);

        return s.httpOps.sendAsync(
            uri,
            s.apiKey,
            s.apiSecret,
            method,
            queryString,
            expiry,
            body,
            Duration.ofSeconds(10L))
            .thenApply(payload -> {
                PhemexResponse<T> resp;
                try {
                    resp = objectMapper.readValue(payload, typeReference);
                } catch (IOException ex) {
                    throw new PhemexException("Failed to parse response message", -1, payload, ex);
                }

                // code = 0 means normal return,o/w, error
                if (resp.code == 0) {
                    return resp;
                } else {
                    throw new PhemexException(resp.getMsg(), resp.getCode(), payload);
                }
            });
    }
}
