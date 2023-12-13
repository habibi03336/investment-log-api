package com.habibi.stockstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StockStoryDto {
    private boolean isLong;
    private String stockCode;
    private int[] stockPrices;
    private LocalDate dt;
    private String story;
}
