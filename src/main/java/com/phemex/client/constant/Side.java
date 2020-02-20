package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Side {

    @JsonEnumDefaultValue
    None,

    Buy,

    Sell;
}
