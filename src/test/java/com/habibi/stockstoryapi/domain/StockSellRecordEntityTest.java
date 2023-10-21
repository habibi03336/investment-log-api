package com.habibi.stockstoryapi.domain;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockSellRecordEntityTest {

    @Test
    public void builder(){
        StockSellRecordEntity stockSellRecordEntity = StockSellRecordEntity.builder().build();
        assertThat(stockSellRecordEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String stockCode = "000660";
        int avgPurchasePrice = 45_600;
        int sellPrice = 892_220;

        // When
        StockSellRecordEntity stockSellRecordEntity = new StockSellRecordEntity();
        stockSellRecordEntity.setStockCode(stockCode);
        stockSellRecordEntity.setAvgPurchasePrice(avgPurchasePrice);
        stockSellRecordEntity.setSellPrice(sellPrice);

        // Then
        assertThat(stockSellRecordEntity.getStockCode()).isEqualTo(stockCode);
        assertThat(stockSellRecordEntity.getAvgPurchasePrice()).isEqualTo(avgPurchasePrice);
        assertThat(stockSellRecordEntity.getSellPrice()).isEqualTo(sellPrice);
    }
}