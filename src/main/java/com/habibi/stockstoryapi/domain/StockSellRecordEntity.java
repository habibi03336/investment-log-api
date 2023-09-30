package com.habibi.stockstoryapi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "stockSellId")
public class StockSellRecordEntity {
    private long stockSellId;
    private long storyId;
    private long userId;
    private String stockCode;
    private long avgPurchasePrice;
    private long sellPrice;
    private LocalDateTime sellDt;
}
