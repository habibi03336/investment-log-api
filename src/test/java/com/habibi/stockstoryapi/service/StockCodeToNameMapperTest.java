package com.habibi.stockstoryapi.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StockCodeToNameMapperTest {
    private StockCodeToNameMapper stockCodeToNameMapper;

    @BeforeEach
    void setUp() {
        stockCodeToNameMapper = new StockCodeToNameMapperByFinanceAPI();
    }

    @Test
    public void nameMappingTest(){
        String TSTrillionStockCode = "317240";
        String TSTrillionName = "TS트릴리온";

        String stockName = stockCodeToNameMapper.getStockName(TSTrillionStockCode);
        assertThat(stockName).isEqualTo(TSTrillionName);
    }

}
