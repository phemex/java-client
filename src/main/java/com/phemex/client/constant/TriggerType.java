package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TriggerType {

    @JsonEnumDefaultValue
    UNSPECIFIED,

    ByMarkPrice,

    ByIndexPrice,

    ByLastPrice;


}
