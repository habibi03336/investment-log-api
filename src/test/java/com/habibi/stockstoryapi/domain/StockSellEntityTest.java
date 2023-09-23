package com.habibi.stockstoryapi.domain;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockSellEntityTest {

    @Test
    public void builder(){
        StockSellEntity stockSellEntity = StockSellEntity.builder().build();
        assertThat(stockSellEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String stockCode = "000660";
        long avgPurchasePrice = 45_600;
        long sellPrice = 892_220;

        // When
        StockSellEntity stockSellEntity = new StockSellEntity();
        stockSellEntity.setStockCode(stockCode);
        stockSellEntity.setAvgPurchasePrice(avgPurchasePrice);
        stockSellEntity.setSellPrice(sellPrice);

        // Then
        assertThat(stockSellEntity.getStockCode()).isEqualTo(stockCode);
        assertThat(stockSellEntity.getAvgPurchasePrice()).isEqualTo(avgPurchasePrice);
        assertThat(stockSellEntity.getSellPrice()).isEqualTo(sellPrice);
    }
}