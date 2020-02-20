package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TimeInForce {
    @JsonEnumDefaultValue
    UNSPECIFIED,

    GoodTillCancel,

    PostOnly,

    ImmediateOrCancel,

    FillOrKill
}
