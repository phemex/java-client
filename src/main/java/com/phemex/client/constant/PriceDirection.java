package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum PriceDirection {

    @JsonEnumDefaultValue
    UNSPECIFIED,

    Rising,

    Falling;
}
