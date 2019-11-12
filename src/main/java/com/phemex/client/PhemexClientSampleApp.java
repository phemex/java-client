package com.phemex.client;

import com.phemex.client.message.CreateOrderRequest;
import com.phemex.client.message.PhemexResponse;
import com.phemex.client.ws.PhemexMessageListener;
import com.phemex.client.ws.PhemexWebSocketClient;

import java.time.Duration;

public class PhemexClientSampleApp {

    public static void main(String[] args) throws Exception {
        String url = "https://testnet-api.phemex.com";
        String wsUri = "wss://testnet.phemex.com/ws/";

        PhemexMessageListener myListener = new PhemexMessageListener() {
            @Override
            public void onMessage(String json) {
                System.out.println("-------- Phemex WebSocket Message: " + json);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("-------- Phemex WebSocket ERROR: " + ex);
            }
        };

        PhemexClient client = PhemexClient.builder().accessToken("xxx")
                .secretKey("xxx")
                .url(url)
                .connectionTimeout(Duration.ofSeconds(600))
                .expiryDuration(Duration.ofSeconds(600))
                .wsUri(wsUri)
                .messageListener(myListener)
                .build();


        subscribeWebSocket(client);

        System.out.println("Start test order...");
        createBTCOrder(client);
        createETHOrder(client);
        cancelOrder(client);
        cancelAllOrder(client);

        Thread.sleep(10_000);
    }

    private static void subscribeWebSocket(PhemexClient client) throws Exception {
        PhemexWebSocketClient webSocketClient = client.createWebSocketClient();
        webSocketClient.connectBlocking();
        webSocketClient.login();
        webSocketClient.subscribeOrderBook("BTCUSD");
        webSocketClient.subscribeAccountDetails();
        Thread.sleep(100000);
        webSocketClient.closeBlocking();
    }

    private static void cancelOrder(PhemexClient client) throws InterruptedException, java.util.concurrent.ExecutionException {
        System.out.println("Start Cancel Order ...");
        PhemexResponse<String> response = client.orders().cancelOrder("1fff82c1-dbf7-4f01-a4da-58fa35a75dd3", "BTCUSD").get();
        System.out.println("Cancel Order Response: " + response);
    }

    private static void cancelAllOrder(PhemexClient client) throws InterruptedException, java.util.concurrent.ExecutionException {
        System.out.println("Start Cancel All Orders ...");
        PhemexResponse<String> response = client.orders().cancelAll().get();
        System.out.println("Cancel Order Response: " + response);
    }

    private static void createBTCOrder(PhemexClient client) throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setActionBy("FromOrderPlacement");
        request.setSymbol("BTCUSD");
        request.setClOrdID(java.util.UUID.randomUUID().toString());
        request.setSide("Buy");
        request.setPriceEp(99000000L);
        request.setOrderQty(Long.valueOf((1)));
        request.setOrdType("Limit");
        request.setReduceOnly(false);
        request.setTriggerType("UNSPECIFIED");
        request.setPegPriceType("UNSPECIFIED");
        request.setTimeInForce("GoodTillCancel");
        request.setTakeProfitEp(0L);
        request.setStopLossEp(0L);
        PhemexResponse<String> response = client.orders().createOrder(request).get();
        if (response.code != 0) {
            throw new RuntimeException("Failed to create order, error code:" + response.code + ", msg: " + response.getMsg());
        }
        System.out.println("Create Order Request: " + request + ", Response:" + response);
    }


    private static void createETHOrder(PhemexClient client) throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setActionBy("FromOrderPlacement");
        request.setSymbol("ETHUSD");
        request.setClOrdID("uuid-" + System.currentTimeMillis() + "" + System.nanoTime());
        request.setSide("Buy");
        request.setPriceEp(1829500L);
        request.setOrderQty(Long.valueOf(1));
        request.setOrdType("Limit");
        request.setReduceOnly(false);
        request.setTriggerType("UNSPECIFIED");
        request.setPegPriceType("UNSPECIFIED");
        request.setTimeInForce("GoodTillCancel");
        request.setTakeProfitEp(0L);
        request.setStopLossEp(0L);
        PhemexResponse<String> response = client.orders().createOrder(request).get();
        if (response.code != 0) {
            throw new RuntimeException("Failed to create order, error code:" + response.code + ", msg: " + response.getMsg());
        }
        System.out.println("Create Order Request: " + request + ", Response:" + response);
    }

}
