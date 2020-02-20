package com.phemex.client.constant;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TradeType {

    @JsonEnumDefaultValue
    UNSPECIFIED,

    Trade,

    Liq,

    Adl,

    Funding,

    TakeOver,

    LiqTrade,

    AdlTrade,

    Amend,

    Cancel,

    Replace,

    TransferIn,

    ExchangeIn,

    ExchangeOut,

    PositionSettingChange,

}
