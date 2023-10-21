package com.habibi.stockstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StockPositionStoryDto {
    private String stockCode;
    private int[] stockPrices;
    private LocalDateTime dt;
    private String story;
}
