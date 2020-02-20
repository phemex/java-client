package com.phemex.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phemex.client.domain.AccountPositionVo;
import com.phemex.client.httpops.ApiState;
import com.phemex.client.message.PhemexResponse;

import com.phemex.client.rest.Account;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class AccountImpl implements Account {

    private final ApiState apiState;

    public AccountImpl(ApiState apiState) {
        this.apiState = apiState;
    }

    public CompletableFuture<PhemexResponse<String>> depositAddress(String currency) {
        return ClientUtils.sendRequest(
            apiState,
            "/phemex-user/wallets/depositAddress",
            "currency=" + currency,
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
            "currency=" + currency,
            "GET",
            null,
            new TypeReference<PhemexResponse<AccountPositionVo>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<String>> changeLeverage(String symbol, long leverageEr) {
        return ClientUtils.sendRequest(
            apiState,
            "/positions/leverage",
            "symbol=" + Objects.requireNonNull(symbol) + "&leverageEr=" + leverageEr,
            "PUT",
            null,
            new TypeReference<PhemexResponse<String>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<String>> changeRiskLimit(String symbol, long riskLimitEv) {
        return ClientUtils.sendRequest(
            apiState,
            "/positions/riskLimit",
            "symbol=" + Objects.requireNonNull(symbol) + "&riskLimitEv=" + riskLimitEv,
            "PUT",
            null,
            new TypeReference<PhemexResponse<String>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<String>> assignPositionBalance(String symbol, long posBalanceEv) {
        return ClientUtils.sendRequest(
            apiState,
            "/positions/assign",
            "symbol=" + Objects.requireNonNull(symbol) + "&posBalanceEv=" + posBalanceEv,
            "PUT",
            null,
            new TypeReference<PhemexResponse<String>>() {
            }
        );
    }

}
