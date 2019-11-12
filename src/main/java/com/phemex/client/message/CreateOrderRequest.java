package com.phemex.client.message;


public class CreateOrderRequest {

    private String actionBy;

    //Instrument symbol,e.g. BTCUSD", required = true
    private String symbol;

    //Buy or Sell"
    private String side;

    //Order quantity"
    private Long orderQty;

    //priceEp", required = false
    private Long priceEp;

    private Long displayQty;


    //Trigger price for stop orders, StopLimit,MarketIfTouched, or LimitIfTouched"
    private Long stopPxEp;

    //client order id"
    private String clOrdID;

    //trailing offset from current price of stop orders"
    private Long pegOffsetValueEp;

    //peg price type, e.g. LastPeg, MidPricePeg, MarketPeg, PrimaryPeg, TrailingStopPeg."
    private String pegPriceType ;

    //order types, default to Limit", allowableValues = "Market, Limit, Stop, StopLimit, MarketIfTouched, LimitIfTouched, Pegged"
    private String ordType ;

    //Time in force. default to GoodTillCancel", allowableValues = "Day,GoodTillCancel, ImmediateOrCancel, FillOrKill"
    private String timeInForce ;


    private Boolean freeUpMargin;

    private Long takeProfitEp;

    private Long stopLossEp;

    private String triggerType ;


    private Boolean reduceOnly;

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
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

    public Long getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Long orderQty) {
        this.orderQty = orderQty;
    }

    public Long getPriceEp() {
        return priceEp;
    }

    public void setPriceEp(Long priceEp) {
        this.priceEp = priceEp;
    }

    public Long getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(Long displayQty) {
        this.displayQty = displayQty;
    }

    public Long getStopPxEp() {
        return stopPxEp;
    }

    public void setStopPxEp(Long stopPxEp) {
        this.stopPxEp = stopPxEp;
    }

    public String getClOrdID() {
        return clOrdID;
    }

    public void setClOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
    }

    public Long getPegOffsetValueEp() {
        return pegOffsetValueEp;
    }

    public void setPegOffsetValueEp(Long pegOffsetValueEp) {
        this.pegOffsetValueEp = pegOffsetValueEp;
    }

    public String getPegPriceType() {
        return pegPriceType;
    }

    public void setPegPriceType(String pegPriceType) {
        this.pegPriceType = pegPriceType;
    }

    public String getOrdType() {
        return ordType;
    }

    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public Boolean getFreeUpMargin() {
        return freeUpMargin;
    }

    public void setFreeUpMargin(Boolean freeUpMargin) {
        this.freeUpMargin = freeUpMargin;
    }

    public Long getTakeProfitEp() {
        return takeProfitEp;
    }

    public void setTakeProfitEp(Long takeProfitEp) {
        this.takeProfitEp = takeProfitEp;
    }

    public Long getStopLossEp() {
        return stopLossEp;
    }

    public void setStopLossEp(Long stopLossEp) {
        this.stopLossEp = stopLossEp;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Boolean getReduceOnly() {
        return reduceOnly;
    }

    public void setReduceOnly(Boolean reduceOnly) {
        this.reduceOnly = reduceOnly;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "actionBy='" + actionBy + '\'' +
                ", symbol='" + symbol + '\'' +
                ", side='" + side + '\'' +
                ", orderQty=" + orderQty +
                ", priceEp=" + priceEp +
                ", displayQty=" + displayQty +
                ", stopPxEp=" + stopPxEp +
                ", clOrdID='" + clOrdID + '\'' +
                ", pegOffsetValueEp=" + pegOffsetValueEp +
                ", pegPriceType='" + pegPriceType + '\'' +
                ", ordType='" + ordType + '\'' +
                ", timeInForce='" + timeInForce + '\'' +
                ", freeUpMargin=" + freeUpMargin +
                ", takeProfitEp=" + takeProfitEp +
                ", stopLossEp=" + stopLossEp +
                ", triggerType='" + triggerType + '\'' +
                ", reduceOnly=" + reduceOnly +
                '}';
    }
}
