package com.phemex.client.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  KlineInterval {

    UNKNOWN(0),

    MINUTE_1(60),

    MINUTE_5(300),

    MINUTE_15(900),

    MINUTE_30(1800),

    HOUR_1(3600),

    HOUR_4(14400),

    DAY_1(86400),

    WEEK_1(604800),

    MONTH_1(2592000),

    SEASON_1(7776000),

    YEAR_1(31104000);

    @Getter
    private int interval;
}
