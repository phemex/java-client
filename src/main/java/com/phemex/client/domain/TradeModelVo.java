package com.phemex.client.domain;

import com.phemex.client.constant.OrdType;
import com.phemex.client.constant.Side;
import com.phemex.client.constant.TradeType;
import lombok.Data;

@Data
public class TradeModelVo {

    private String symbol;

    private String currency;

    private String action;

    private Side side;

    private TradeType tradeType;

    private long execQty;

    private long execPriceEp;

    private long orderQty;

    private long priceEp;

    private long execValueEv;

    private long feeRateEr;

    private long execFeeEv;

    private OrdType ordType;

    private String orderID;

    private String clOrdID;

    private String execStatus;

    private long transactTimeNs;
}
