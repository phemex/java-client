package com.phemex.client.httpops.httpurlconnection;

import com.phemex.client.domain.PhemexApiConstant;
import com.phemex.client.httpops.HttpOps;
import com.phemex.client.httpops.HttpOpsBuilder;
import com.phemex.client.impl.ClientUtils;
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
        int connectTimeout = (int) this.connectionTimeout.toMillis() / 1000;

        return new HttpOps() {
            @Override
            public CompletableFuture<String> sendAsync(URI uri, String accessToken, byte[] secretKey,
                                                       String method, String queryString, long expiry,
                                                       String body, Duration timeout) {

                CompletableFuture<String> f = new CompletableFuture<>();

                HttpURLConnection conn;
                URI withQueryUri;
                try {
                    withQueryUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), queryString, uri.getFragment());
                    conn = (HttpURLConnection) withQueryUri.toURL().openConnection();
                } catch (IOException | URISyntaxException e) {
                    f.completeExceptionally(e);
                    return f;
                }
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(connectTimeout*10);

                try {
                    conn.setRequestMethod(method);
                } catch (ProtocolException e) {
                    f.completeExceptionally(e);
                    return f;
                }

                if (accessToken != null) {
                    conn.setRequestProperty(PhemexApiConstant.PHEMEX_HEADER_REQUEST_EXPIRY, expiry + "");
                    conn.setRequestProperty(PhemexApiConstant.PHEMEX_HEADER_REQUEST_ACCESS_TOKEN, accessToken);
                    conn.setRequestProperty(PhemexApiConstant.PHEMEX_HEADER_REQUEST_SIGNATURE,
                            ClientUtils.sign(withQueryUri.getPath(), queryString, expiry, body, secretKey));
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
                    f.completeExceptionally(new RuntimeException("non 200 response"));
                    return f;
                }
                try (InputStream is = conn.getInputStream()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] b = new byte[128];
                    int read;
                    while ((read = is.read(b)) != -1) {
                        baos.write(b, 0, read);
                    }
                    f.complete(new String(baos.toByteArray(), StandardCharsets.UTF_8));
                    return f;
                } catch (IOException e) {
                    f.completeExceptionally(e);
                    return f;
                }
            }
        };
    }
}
