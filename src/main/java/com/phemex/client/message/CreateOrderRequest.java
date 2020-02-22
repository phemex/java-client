package com.phemex.client.message;


import com.phemex.client.constant.OrdType;
import com.phemex.client.constant.PegPriceType;
import com.phemex.client.constant.Side;
import com.phemex.client.constant.TimeInForce;
import com.phemex.client.constant.TriggerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequest {

    private String symbol;

    private Side side;// Buy, Sell

    private Long orderQty;

    private Long priceEp;

    private Long stopPxEp;

    private String clOrdID;

    private Long pegOffsetValueEp;

    private PegPriceType pegPriceType;

    private OrdType ordType; //Market, Limit, Stop, StopLimit,MarketAsLimit,StopAsLimit,MarketIfTouchedAsLimit

    private TimeInForce timeInForce;//GoodTillCancel, PostOnly, ImmediateOrCancel, FillOrKill

    private boolean reduceOnly;

    private boolean closeOnTrigger;

    private Long takeProfitEp;

    private Long stopLossEp;

    private Long trailingStopPxEp;

    private TriggerType triggerType;// ByMarkPrice, ByIndexPrice, ByLastPrice

    private String text;

}
