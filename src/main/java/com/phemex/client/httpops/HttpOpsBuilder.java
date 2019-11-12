package com.phemex.client.httpops;

import com.phemex.client.httpops.httpurlconnection.HttpUrlConnectionOpsBuilder;

import java.time.Duration;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;

public interface HttpOpsBuilder {
    HttpOpsBuilder executor(Executor e);

    HttpOpsBuilder connectionTimeout(Duration to);

    HttpOps build();

    static HttpOpsBuilder newBuilder() {
        return new HttpUrlConnectionOpsBuilder();
    }
}
