package com.phemex.client.rest;

import com.phemex.client.domain.OrderModelVo;
import com.phemex.client.domain.PagedResult;
import com.phemex.client.domain.TradeModelVo;
import com.phemex.client.domain.WebResultVo;
import com.phemex.client.message.CreateOrderRequest;
import com.phemex.client.message.PhemexResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Order {

    CompletableFuture<PhemexResponse<OrderModelVo>> createOrder(CreateOrderRequest createOrderRequest);

    CompletableFuture<PhemexResponse<OrderModelVo>> amendOrder(String symbol, String orderID, String origClOrdID, String clOrdID, long priceEp, long orderQty, long stopPxEp,
        long takeProfitEp, long stopLossEp, long pegOffsetEp);

    CompletableFuture<PhemexResponse<OrderModelVo>> cancelOrder(String symbol, String orderID);
    
    CompletableFuture<PhemexResponse<String>> cancelAll(String symbol, boolean untriggered, String text);

    CompletableFuture<PhemexResponse<WebResultVo>> listOpenOrders(String symbol);

    CompletableFuture<PhemexResponse<OrderModelVo>> queryOpenOrder(String symbol, String orderID);

    CompletableFuture<PhemexResponse<List<OrderModelVo>>> queryOrder(String symbol, String orderID);

    CompletableFuture<PhemexResponse<List<OrderModelVo>>> queryOrderByClOrdID(String symbol, String clOrdID);

    CompletableFuture<PhemexResponse<PagedResult<OrderModelVo>>> listHistoryOrders(String symbol, long start, long end, long offset, int limit);

    CompletableFuture<PhemexResponse<PagedResult<TradeModelVo>>> listTrades(String symbol, long start, long end, long offset, int limit, List<String> tradeType);
}
