package com.habibi.stockstoryapi.domain;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockPurchaseRecordEntityTest {

    @Test
    public void builder(){
        StockPurchaseRecordEntity stockPurchaseRecordEntity = StockPurchaseRecordEntity.builder().build();
        assertThat(stockPurchaseRecordEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String stockCode = "000660";
        int purchasePrice = 45_600;

        // When
        StockPurchaseRecordEntity stockPurchaseRecordEntity = new StockPurchaseRecordEntity();
        stockPurchaseRecordEntity.setStockCode(stockCode);
        stockPurchaseRecordEntity.setPurchasePrice(purchasePrice);

        // Then
        assertThat(stockPurchaseRecordEntity.getStockCode()).isEqualTo(stockCode);
        assertThat(stockPurchaseRecordEntity.getPurchasePrice()).isEqualTo(purchasePrice);
    }
}