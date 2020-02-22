package com.phemex.client.domain;

import lombok.Data;

@Data
public class PositionVo {

    private String symbol;

    private String currency;

    private String side;

    private String positionStatus;

    private boolean crossMargin;

    private long leverageEr;

    private long size;

    private long valueEv;

    private long avgEntryPriceEp;

    private long posCostEv;

}
