package com.habibi.stockstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StockStateDto {
    private int count;
    private long totalPurchasePrice;

    public int getAveragePurchasePrice(){
        return (int) (totalPurchasePrice / count);
    }
}
