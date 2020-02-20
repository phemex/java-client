package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum PegPriceType {

    @JsonEnumDefaultValue
    UNSPECIFIED,

    LastPeg,

    MidPricePeg,

    MarketPeg,

    PrimaryPeg,

    TrailingStopPeg,
}
