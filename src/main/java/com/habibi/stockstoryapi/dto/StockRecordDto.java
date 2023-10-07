package com.habibi.stockstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StockRecordDto {
    private String stockCode;
    private LocalDateTime dt;
    private String stockPrice;
}
