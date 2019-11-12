package com.phemex.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phemex.client.domain.AccountPositionVo;
import com.phemex.client.httpops.ApiState;
import com.phemex.client.message.PhemexResponse;

import java.util.concurrent.CompletableFuture;

public class AccountImpl {

    private final ApiState apiState;

    public AccountImpl(ApiState apiState) {
        this.apiState = apiState;
    }


    public CompletableFuture<PhemexResponse<String>> depositAddress(String currency) {
        return ClientUtils.sendRequest(
                apiState,
                "/phemex-user/wallets/depositAddress",
                "currency="+currency,
                "GET",
                null,
                new TypeReference<PhemexResponse<String>>() {
                }
        );
    }

    public CompletableFuture<PhemexResponse<AccountPositionVo>> queryAccountPosition(String currency) {
        return ClientUtils.sendRequest(
                apiState,
                "/accounts/accountPositions",
                "currency="+currency,
                "GET",
                null,
                new TypeReference<PhemexResponse<AccountPositionVo>>() {
                }
        );
    }

}
