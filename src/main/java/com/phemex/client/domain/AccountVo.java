package com.phemex.client.domain;

import lombok.Data;

@Data
public class AccountVo {

    private String currency;

    private long accountBalanceEv;

    private long totalUsedBalanceEv;
}
