package com.phemex.client.httpops.httpurlconnection;

import com.phemex.client.constant.PhemexApiConstant;
import com.phemex.client.exceptions.PhemexException;
import com.phemex.client.httpops.HttpOps;
import com.phemex.client.httpops.HttpOpsBuilder;
import com.phemex.client.impl.ClientUtils;
import com.phemex.client.message.PhemexResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class HttpUrlConnectionOpsBuilder implements HttpOpsBuilder {

    static final Logger logger = LoggerFactory.getLogger(HttpUrlConnectionOpsBuilder.class);

    Duration connectionTimeout = Duration.ofSeconds(5L);

    @Override
    public HttpOpsBuilder executor(Executor e) {
        logger.warn("ignoring executor because async requests are not supported using HTTPURLConnection");
        return this;
    }

    @Override
    public HttpOpsBuilder connectionTimeout(Duration to) {
        connectionTimeout = Objects.requireNonNull(to);
        return this;
    }

    @Override
    public HttpOps build() {
        int connectTimeout = (int) this.connectionTimeout.toMillis();

        return new HttpOps() {
            @Override
            public CompletableFuture<String> sendAsync(URI uri, String apiKey, byte[] apiSecret,
                String method, String queryString, long expiry,
                String body, Duration timeout) {

                CompletableFuture<String> f = new CompletableFuture<>();

                HttpURLConnection conn;
                URI withQueryUri;
                try {
                    withQueryUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), queryString, uri.getFragment());
//                    logger.debug("Opening http connection to url {}", withQueryUri);
                    conn = (HttpURLConnection) withQueryUri.toURL().openConnection();
                } catch (IOException | URISyntaxException e) {
                    f.completeExceptionally(e);
                    return f;
                }
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(connectTimeout * 10);

                try {
                    conn.setRequestMethod(method);
                } catch (ProtocolException e) {
                    f.completeExceptionally(e);
                    return f;
                }
                long expry = expiry / 1_000L;

                if (apiKey != null) {
                    conn.setRequestProperty(PhemexApiConstant.PHEMEX_HEADER_REQUEST_EXPIRY, expry + "");
                    conn.setRequestProperty(PhemexApiConstant.PHEMEX_HEADER_REQUEST_ACCESS_TOKEN, apiKey);
                    conn.setRequestProperty(PhemexApiConstant.PHEMEX_HEADER_REQUEST_SIGNATURE,
                        ClientUtils.sign(withQueryUri.getPath(), queryString, expry, body, apiSecret));
                }

                if (body != null) {
                    conn.setRequestProperty("content-type", "application/json");
                    conn.setDoOutput(true);
                    OutputStream os = null;
                    try {
                        os = conn.getOutputStream();
                        os.write(body.getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    } catch (IOException e) {
                        f.completeExceptionally(e);
                        return f;
                    }
                }

                int respCode;
                try {
                    long st = System.currentTimeMillis();
                    respCode = conn.getResponseCode();
                    //System.out.println("One round trip latency is: \t\t" + (System.currentTimeMillis()-st) );
                } catch (IOException e) {
                    f.completeExceptionally(e);
                    return f;
                }
                if (respCode != 200) {
                    try {
                        f.completeExceptionally(toPhemexException(IOUtils.toString(conn.getErrorStream(), StandardCharsets.UTF_8)));
                    } catch (IOException e) {
                        logger.warn("Got exception reading error output stream", e);
                    }
                    return f;
                }

                try (InputStream is = conn.getInputStream()) {
                    f.complete(IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8));
                    return f;
                } catch (IOException e) {
                    f.completeExceptionally(new PhemexException("", -1, null, e));
                    return f;
                }
            }
        };
    }

    private PhemexException toPhemexException(String str) {
        logger.debug("Got exception str {}", str);
        try {
            PhemexResponse res = ClientUtils.objectMapper.readValue(str, PhemexResponse.class);
            return new PhemexException(res.getMsg(), res.getCode(), null);
        } catch (IOException ex) {
            logger.warn("Failed to translate to phemex response", ex);
        }
        return new PhemexException(str, -1, null);
    }
}
