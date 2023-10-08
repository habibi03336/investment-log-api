package com.habibi.stockstoryapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "stockSellId")
@Entity
public class StockSellRecordEntity {
    @Id @GeneratedValue
    private long stockSellId;
    private long storyId;
    private long userId;
    private String stockCode;
    private long avgPurchasePrice;
    private long sellPrice;
    private LocalDateTime sellDt;
}
