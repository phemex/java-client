package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum OrdType {

    @JsonEnumDefaultValue
    UNSPECIFIED,

    Market,

    Limit,

    Stop,

    StopLimit,

    MarketIfTouched,

    LimitIfTouched,

    StopAsLimit,

    MarketIfTouchedAsLimit;
}
