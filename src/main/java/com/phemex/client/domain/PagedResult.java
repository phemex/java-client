package com.phemex.client.domain;

import java.util.List;
import lombok.Data;

@Data
public class PagedResult<T> {

    private long total;

    private List<T> rows;

}
