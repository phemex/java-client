package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum OrdStatus {

    @JsonEnumDefaultValue
    Created,

    Untriggered,

    Deactivated,

    Triggered,

    Rejected,

    New,

    PartiallyFilled,

    Filled,

    Canceled;

}
