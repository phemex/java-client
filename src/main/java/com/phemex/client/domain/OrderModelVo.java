package com.phemex.client.domain;


public class OrderModelVo {

    private String orderID;

    private String clOrdID;

    private long accountID;

    private String symbol;

    private String side;

    private String orderType;

    private long priceEp;

    private long orderQty;


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClOrdID() {
        return clOrdID;
    }

    public void setClOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public long getPriceEp() {
        return priceEp;
    }

    public void setPriceEp(long priceEp) {
        this.priceEp = priceEp;
    }

    public long getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(long orderQty) {
        this.orderQty = orderQty;
    }
}
