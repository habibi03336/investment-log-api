package com.habibi.stockstoryapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "stockSellId")
@Entity
public class StockSellRecordEntity {
    @Id @GeneratedValue
    private long stockSellId;
    private long storyId;
    private int userId;
    private String stockCode;
    private int avgPurchasePrice;
    private int sellPrice;
    private LocalDate sellDt;
}
