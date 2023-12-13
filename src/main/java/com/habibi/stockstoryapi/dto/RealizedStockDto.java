package com.habibi.stockstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RealizedStockDto {
    private String stockName;
    private String stockCode;
    private Integer stockCount;
    private Integer averageSellPrice;
    private Integer averagePurchasePrice;
}
