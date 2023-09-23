package com.habibi.stockstoryapi.domain;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockPurchaseEntityTest {

    @Test
    public void builder(){
        StockPurchaseEntity stockPurchaseEntity = StockPurchaseEntity.builder().build();
        assertThat(stockPurchaseEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String stockCode = "000660";
        long purchasePrice = 45_600;

        // When
        StockPurchaseEntity stockPurchaseEntity = new StockPurchaseEntity();
        stockPurchaseEntity.setStockCode(stockCode);
        stockPurchaseEntity.setPurchasePrice(purchasePrice);

        // Then
        assertThat(stockPurchaseEntity.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPurchaseEntity.getPurchasePrice()).isEqualTo(purchasePrice);
    }
}