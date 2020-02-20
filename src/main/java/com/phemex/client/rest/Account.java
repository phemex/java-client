package com.phemex.client.rest;

import com.phemex.client.domain.AccountPositionVo;
import com.phemex.client.message.PhemexResponse;
import java.util.concurrent.CompletableFuture;

public interface Account {

    CompletableFuture<PhemexResponse<AccountPositionVo>> queryAccountPosition(String currency);

    CompletableFuture<PhemexResponse<String>> changeLeverage(String symbol, long leverageEr);

    CompletableFuture<PhemexResponse<String>> changeRiskLimit(String symbol, long riskLimitEv);

    CompletableFuture<PhemexResponse<String>> assignPositionBalance(String symbol, long balanceEv);

}
