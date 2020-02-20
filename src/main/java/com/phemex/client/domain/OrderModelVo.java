package com.phemex.client.domain;

import com.phemex.client.constant.OrdStatus;
import com.phemex.client.constant.OrdType;
import com.phemex.client.constant.PriceDirection;
import com.phemex.client.constant.Side;
import com.phemex.client.constant.TimeInForce;
import lombok.Data;

@Data
public class OrderModelVo {

    private int bizError;

    private String orderID;

    private String clOrdID;

    private String symbol;

    private Side side;

    private OrdType orderType;

    private long priceEp;

    private long orderQty;

    private long displayQty;

    private TimeInForce timeInForce;

    private boolean reduceOnly;

    private long takeProfitEp;

    private long stopLossEp;

    private long closedPnlEv;

    private long closedSize;

    private long cumQty;

    private long cumValueEv;

    private long leavesQty;

    private long leavesValueEv;

    private PriceDirection stopDirection;

    private OrdStatus ordStatus;

}
