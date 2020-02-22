package com.phemex.client.impl;

import com.phemex.client.domain.OrderModelVo;
import com.phemex.client.domain.PagedResult;
import com.phemex.client.domain.TradeModelVo;
import com.phemex.client.domain.WebResultVo;
import com.phemex.client.message.PhemexResponse;
import com.phemex.client.httpops.ApiState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phemex.client.message.CreateOrderRequest;

import com.phemex.client.rest.Order;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class OrderImpl implements Order {

    final ApiState apiState;

    public OrderImpl(ApiState apiState) {
        this.apiState = apiState;
    }

    @Override
    public CompletableFuture<PhemexResponse<OrderModelVo>> createOrder(CreateOrderRequest createOrderRequest) {
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
            new TypeReference<PhemexResponse<OrderModelVo>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<OrderModelVo>> amendOrder(String symbol,
        String orderID,
        String origClOrdID,
        String clOrdID,
        long priceEp,
        long orderQty,
        long stopPxEp,
        long takeProfitEp,
        long stopLossEp,
        long pegOffsetEp
    ) {
        StringBuilder qStrBuilder = new StringBuilder();
        qStrBuilder.append("symbol=" + Objects.requireNonNull(symbol));
        qStrBuilder.append("&orderID=" + Objects.requireNonNull(orderID));

        if (origClOrdID != null && clOrdID != null) {
            qStrBuilder.append("&origClOrdID=" + origClOrdID);
            qStrBuilder.append("&clOrdID=" + clOrdID);
        }

        if (priceEp != 0) {
            qStrBuilder.append("&priceEp=" + priceEp);
        }

        if (orderQty != 0) {
            qStrBuilder.append("&orderQty=" + orderQty);
        }

        if (stopPxEp != 0) {
            qStrBuilder.append("&stopPxEp=" + stopPxEp);
        }

        if (takeProfitEp != 0) {
            qStrBuilder.append("&takeProfitEp=" + takeProfitEp);
        }

        if (stopLossEp != 0) {
            qStrBuilder.append("&stopLossEp=" + stopLossEp);
        }

        if (pegOffsetEp != 0) {
            qStrBuilder.append("&pegOffsetEp=" + pegOffsetEp);
        }

        return ClientUtils.sendRequest(apiState,
            "/orders/replace",
            qStrBuilder.toString(),
            "PUT",
            null,
            new TypeReference<PhemexResponse<OrderModelVo>>() {
            });
    }

    @Override
    public CompletableFuture<PhemexResponse<OrderModelVo>> cancelOrder(String symbol, String orderID) {
        return ClientUtils.sendRequest(
            apiState,
            "/orders/cancel",
            "symbol=" + Objects.requireNonNull(symbol) + "&orderID=" + Objects.requireNonNull(orderID),
            "DELETE",
            null,
            new TypeReference<PhemexResponse<OrderModelVo>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<OrderModelVo>> queryOpenOrder(String symbol, String orderID) {
        return ClientUtils.sendRequest(
            apiState,
            "/orders/active",
            "symbol=" + Objects.requireNonNull(symbol) + "&orderID=" + Objects.requireNonNull(orderID),
            "GET",
            null,
            new TypeReference<PhemexResponse<OrderModelVo>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<List<OrderModelVo>>> queryOrder(String symbol, String orderID) {
        return ClientUtils.sendRequest(
            apiState,
            "/exchange/order",
            "symbol=" + Objects.requireNonNull(symbol) + "&orderID=" + Objects.requireNonNull(orderID),
            "GET",
            null,
            new TypeReference<PhemexResponse<List<OrderModelVo>>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<List<OrderModelVo>>> queryOrderByClOrdID(String symbol, String clOrdID) {
        return ClientUtils.sendRequest(
            apiState,
            "/exchange/order",
            "symbol=" + Objects.requireNonNull(symbol) + "&clOrdID=" + Objects.requireNonNull(clOrdID),
            "GET",
            null,
            new TypeReference<PhemexResponse<List<OrderModelVo>>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<PagedResult<OrderModelVo>>> listHistoryOrders(String symbol, long start, long end, long offset, int limit) {
        int lmt = limit == 0 ? 50 : limit;
        return ClientUtils.sendRequest(
            apiState,
            "/exchange/order/list",
            "symbol=" + Objects.requireNonNull(symbol) + "&withCount=true" + "&start=" + start + "&end=" + end + "&offset=" + offset + "&limit=" + lmt,
            "GET",
            null,
            new TypeReference<PhemexResponse<PagedResult<OrderModelVo>>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<PagedResult<TradeModelVo>>> listTrades(String symbol, long start, long end, long offset, int limit, List<String> tradeType) {
        int lmt = limit == 0 ? 50 : limit;
        return ClientUtils.sendRequest(
            apiState,
            "/exchange/order/trade",
            "symbol=" + Objects.requireNonNull(symbol) + "&withCount=true" + "&start=" + start + "&end=" + end + "&offset=" + offset + "&limit=" + lmt,
            "GET",
            null,
            new TypeReference<PhemexResponse<PagedResult<TradeModelVo>>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<String>> cancelAll(String symbol, boolean untriggered, String text) {
        String qStr = "symbol=" + Objects.requireNonNull(symbol) + "&untriggered=" + untriggered;
        if (text != null) {
            qStr += "&text=" + text;
        }
        return ClientUtils.sendRequest(
            apiState,
            "/orders/all",
            qStr,
            "DELETE",
            null,
            new TypeReference<PhemexResponse<String>>() {
            }
        );
    }

    @Override
    public CompletableFuture<PhemexResponse<WebResultVo>> listOpenOrders(String symbol) {
        return ClientUtils.sendRequest(
            apiState,
            "/orders/activeList",
            "symbol=" + Objects.requireNonNull(symbol),
            "GET",
            null,
            new TypeReference<PhemexResponse<WebResultVo>>() {
            });
    }
}
