package com.phemex.client.impl;

import com.phemex.client.domain.OrderModelVo;
import com.phemex.client.domain.WebResultVo;
import com.phemex.client.message.PhemexResponse;
import com.phemex.client.httpops.ApiState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phemex.client.message.CreateOrderRequest;

import java.util.concurrent.CompletableFuture;


public class OrderImpl {
    final ApiState apiState;

    public OrderImpl(ApiState apiState) {
        this.apiState = apiState;
    }

    public CompletableFuture<PhemexResponse<String>> createOrder(CreateOrderRequest createOrderRequest) {
        String body = null;
        try {
            body = ClientUtils.objectMapper.writeValueAsString(createOrderRequest);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing request body", e);
        }
        return ClientUtils.sendRequest(
                apiState,
                "/orders",
                null,
                "POST",
                body,
                new TypeReference<PhemexResponse<String>>() {
                }
        );
    }

    public CompletableFuture<PhemexResponse<String>> cancelOrder(String orderID, String symbol) {
        return ClientUtils.sendRequest(
                apiState,
                "/orders",
                "orderID=" + orderID + "&symbol=" + symbol,
                "DELETE",
                null,
                new TypeReference<PhemexResponse<String>>() {
                }
        );
    }

    public CompletableFuture<PhemexResponse<String>> cancelAll() {
        return ClientUtils.sendRequest(
                apiState,
                "/orders/all",
                null,
                "DELETE",
                null,
                new TypeReference<PhemexResponse<String>>() {
                }
        );
    }

    public CompletableFuture<PhemexResponse<WebResultVo>> queryActiveOrder(String currency, String symbol) {
        return ClientUtils.sendRequest(
                apiState,
                "/accounts/orders",
                "currency=" + currency + "&symbol=" + symbol,
                "GET",
                null,
                new TypeReference<PhemexResponse<WebResultVo>>() {
                }
        );
    }

    public CompletableFuture<PhemexResponse<OrderModelVo>> queryHistoryOrder(String orderId) {
        return ClientUtils.sendRequest(
                apiState,
                "/accounts/orders",
                "orderId=" + orderId,
                "GET",
                null,
                new TypeReference<PhemexResponse<OrderModelVo>>() {
                }
        );
    }

}
