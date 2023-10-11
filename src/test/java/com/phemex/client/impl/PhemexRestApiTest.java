package com.phemex.client.impl;

import com.phemex.client.PhemexClient;
import com.phemex.client.constant.OrdType;
import com.phemex.client.constant.Side;
import com.phemex.client.constant.TriggerType;
import com.phemex.client.domain.AccountPositionVo;
import com.phemex.client.domain.OrderModelVo;
import com.phemex.client.domain.PagedResult;
import com.phemex.client.domain.TradeModelVo;
import com.phemex.client.domain.WebResultVo;
import com.phemex.client.message.CreateOrderRequest;
import com.phemex.client.message.PhemexResponse;
import com.phemex.client.ws.PhemexMessageListener;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@Slf4j
public class PhemexRestApiTest {

    private PhemexClient phemexClient;

    String url = "https://testnet-api.phemex.com";

    static private String testnetApiKey = null;

    static private String testnetApiSecret = null;

    String wsUri = "wss://testnet-api.phemex.com/ws/";

    @Before
    public void setup() {
        if(testnetApiKey == null || testnetApiSecret == null) {
            throw new IllegalStateException("Please update apiKey and apiSecret before testing");
        }

        this.phemexClient = PhemexClient.builder()
            .apiKey(testnetApiKey)
            .apiSecret(testnetApiSecret)
            .url(url)
            .connectionTimeout(Duration.ofSeconds(600))
            .expiryDuration(Duration.ofSeconds(60))
            .wsUri(wsUri)
            .messageListener(new PhemexMessageListener() {
                @Override
                public void onMessage(String json) {
                    //ignore
                    log.info("websocket received msg {}", json);
                }

                @Override
                public void onError(Exception ex) {
                    //ignore
                    log.info("websocket got error ", ex);
                }
            })
            .build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        String symbol = "BTCUSD";

        PhemexResponse<OrderModelVo> res = this.phemexClient.orders().createOrder(CreateOrderRequest.builder()
            .symbol(symbol)
            .clOrdID(UUID.randomUUID().toString())
            .orderQty(1L)
            .priceEp(80_000_000L)
            .side(Side.Buy)
            .build()).get(5, TimeUnit.SECONDS);
        log.info("Limit Order created {}", res.getData());

        String clOrdID = UUID.randomUUID().toString();

        PhemexResponse<OrderModelVo> marketOrdRes = this.phemexClient.orders().createOrder(CreateOrderRequest.builder()
            .symbol(symbol)
            .clOrdID(clOrdID)
            .orderQty(1L)
            .ordType(OrdType.Market)
            .side(Side.Buy)
            .build()).get(5, TimeUnit.SECONDS);

        log.info("Market Order created {}", marketOrdRes.getData());

        PhemexResponse<List<OrderModelVo>> queryOrderRes = this.phemexClient.orders().queryOrderByClOrdID(symbol, clOrdID).get(5, TimeUnit.SECONDS);
        log.info("query order res {}", queryOrderRes);

        PhemexResponse<OrderModelVo> amendedOrderRes =
            this.phemexClient.orders().amendOrder(symbol, res.getData().getOrderID(), null, null, 0, 2, 0, 0, 0, 0).get(5, TimeUnit.SECONDS);

        log.info("amended order {}", amendedOrderRes.getData());
    }


    @Test
    public void testListOpenOrder() throws Exception {
        String symbol = "BTCUSD";

        PhemexResponse<OrderModelVo> stopLimitOrderRes = this.phemexClient.orders().createOrder(CreateOrderRequest.builder()
            .ordType(OrdType.Market)
            .symbol(symbol)
            .side(Side.Sell)
            .orderQty(1L)
            .triggerType(TriggerType.ByLastPrice)
            .stopPxEp(79000000L)
            .build()).get(5, TimeUnit.SECONDS);

        PhemexResponse<WebResultVo> openOrdersRes = this.phemexClient.orders().listOpenOrders(symbol).get(5, TimeUnit.SECONDS);

        log.info("open order are {}", openOrdersRes);

        String orderID = openOrdersRes.getData().getRows().get(0).getOrderID();

        PhemexResponse<OrderModelVo> openOrderRes = this.phemexClient.orders().queryOpenOrder(symbol, orderID).get(5, TimeUnit.SECONDS);
        log.info("open order is {}", openOrderRes.getData());

        PhemexResponse<OrderModelVo> cancelOrderRes = this.phemexClient.orders().cancelOrder(symbol, orderID).get(5, TimeUnit.SECONDS);
        log.info("canceled order is {}", cancelOrderRes.getData());

    }

    @Test
    public void testQueryAccountPositions() throws Exception {
        PhemexResponse<AccountPositionVo> accountPositionVoPhemexResponse = this.phemexClient.account().queryAccountPosition("BTC").get(5, TimeUnit.SECONDS);
        log.info("BTC account positions {}", accountPositionVoPhemexResponse.getData());
    }

    @Test
    public void testListHistoryOrders() throws Exception {
        String symbol = "BTCUSD";
        long start = ZonedDateTime.now().minusDays(30).toEpochSecond() * 1000L;
        long end = ZonedDateTime.now().toEpochSecond() * 1000;

        PhemexResponse<PagedResult<OrderModelVo>> orderHistRes = this.phemexClient.orders().listHistoryOrders(symbol, start, end
            , 0, 10).get(5, TimeUnit.SECONDS);
        log.info("BTCUSD history orders {}", orderHistRes.getData());
        if (orderHistRes.getData().getTotal() <= 0) {
            return;
        }

        OrderModelVo order1st = orderHistRes.getData().getRows().get(0);

        PhemexResponse<List<OrderModelVo>> order = this.phemexClient.orders().queryOrder(symbol, order1st.getOrderID()).get(5, TimeUnit.SECONDS);
        log.info("order is {}", order.getData());

        PhemexResponse<PagedResult<TradeModelVo>> tradesRes = this.phemexClient.orders().listTrades(symbol, start, end, 0, 10, null).get(5, TimeUnit.SECONDS);
        log.info("trade hist is {}", tradesRes.getData());
    }

    @Test
    public void testChangeLeverage() throws Exception {
        String symbol = "ETHUSD";
        log.info("change to Cross mode");
        PhemexResponse<String> leverage10xRes = this.phemexClient.account().changeLeverage(symbol, 1_000_000_000L).get(5, TimeUnit.SECONDS);
        log.info("10x leverage change res {}", leverage10xRes);
        PhemexResponse<String> crossLeverageRes = this.phemexClient.account().changeLeverage(symbol, 0).get(5, TimeUnit.SECONDS);
        log.info("cross leverage change res {}", crossLeverageRes);

        this.phemexClient.account().changeRiskLimit(symbol, 3500000);
    }

    @Test
    public void testCancelAllOrder() throws Exception {
        String symbol = "BTCUSD";

        PhemexResponse<OrderModelVo> stopLimitOrderRes = this.phemexClient.orders().createOrder(CreateOrderRequest.builder()
            .ordType(OrdType.LimitIfTouched)
            .symbol(symbol)
            .side(Side.Buy)
            .orderQty(1L)
            .priceEp(80000000L)
            .triggerType(TriggerType.ByLastPrice)
            .stopPxEp(79000000L)
            .build()).get(5, TimeUnit.SECONDS);

        log.info("stop limit order is {}", stopLimitOrderRes);

        log.info("Cancel non-conditional orders");
        PhemexResponse<String> cancelAllRes = this.phemexClient.orders().cancelAll(symbol, false, "test cancel all order").get(5, TimeUnit.SECONDS);
        log.info("cancel all res is {}", cancelAllRes.getData());

        log.info("Cancel conditional order ");
        PhemexResponse<String> conditionaOrderCancelRes = this.phemexClient.orders().cancelAll(symbol, true, "test cancel all order").get(5, TimeUnit.SECONDS);
        log.info("cancel conditional order res {}", conditionaOrderCancelRes.getData());

    }
}