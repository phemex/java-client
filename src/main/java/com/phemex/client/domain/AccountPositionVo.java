package com.phemex.client.domain;

import java.util.List;
import lombok.Data;

@Data
public class AccountPositionVo {

    private AccountVo account;

    private List<PositionVo> positions;

}
