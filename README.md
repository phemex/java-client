
## Phemex Exchange Java API

   * Phemex Java API provides ***Trading Rest APIs*** and ***WebSocket*** to the [exchange](https://phemex.com), a full list of APIs is in the official [documentation](https://github.com/phemex/phemex-api-docs).
   * API Key & Secret is created in the [exchange website](https://phemex.com) or testnet (https://testnet.phemex.com)
   * API Base path of exchange is `https://api.phemex.com`, testnet is `https://testnet-api.phemex.com`

## Building from Source

### Prerequisites
   * Java 1.8+
   * Gradle 5.6+
   
### Compile And Test

```
gradle build
```

## Quick Start

   * Create a `PhemexClient` with your `apiKey` and `apiSecret` 
   * `PhemexClient::orders` for orders related API, `PhemexClient::account` for `Positions` and `Accounts` related API, `PhemexClient::createWebSocketClient` for WebSocket


   * Create a PhemexClient
   
```
PhemexClient createPhemexClient() {
    String apiKey = "YOUR_API_KEY";

    String apiSecret = "YOUR_API_SECRET";

    return PhemexClient.builder()
        .apiKey(apiKey)
        .apiSecret(apiSecret)
        .url(url)
        .connectionTimeout(Duration.ofSeconds(600))
        .expiryDuration(Duration.ofSeconds(60))
        .wsUri(wsUri)
        .messageListener(new PhemexMessageListener() {
            @Override
            public void onMessage(String json) {
                log.info("websocket received msg {}", json);
            }

            @Override
            public void onError(Exception ex) {
                log.info("websocket got error ", ex);
            }
        })
        .build();
}
```

   * Create an order
   
```
        // create a order
void placeOrder() {
    PhemexResponse<OrderModelVo> res = this.phemexClient.orders().createOrder(CreateOrderRequest.builder()
        .symbol(symbol)
        .clOrdID(UUID.randomUUID().toString())
        .orderQty(1L)
        .priceEp(80_000_000L)
        .side(Side.Buy)
        .build()).get(5, TimeUnit.SECONDS);
}

```

   * WebSocket subscribe user accounts, positions, orders and trades
   
```
void subscribeAop() {
    this.webSocketClient = this.phemexClient.createWebSocketClient();
    webSocketClient.connectBlocking(5, TimeUnit.SECONDS);
    webSocketClient.login();
    this.webSocketClient.subscribeAccountDetails();
}
```

## Documentation
   * [Full documentation](https://github.com/phemex/phemex-api-docs)
### Rest Feature List
1. Place Order
1. Amend Order by OrderID
1. Cancel Single Order by OrderID
1. Cancel All Orders by Symbol
1. Query Trading Account and Positions
1. Change Position Leverage
1. Change Position Risklimt
1. Assign Position Balance in Isolated Margin Mode
1. Query Open Orders by Symbol
1. Query Closed Orders by Symbol
1. Query Order by orderID
1. Query User Trades by Symbol

### WebSocket Feature List

1. API User Authentication
1. Subscribe OrderBook
1. Subscribe Trade
1. Subscribe Account-Order-Position (AOP)
1. Subscribe symbol price




