package com.habibi.stockstoryapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "stockPurchaseId")
@Entity
public class StockPurchaseRecordEntity {
    @Id @GeneratedValue
    private long stockPurchaseId;
    private long storyId;
    private long userId;
    private String stockCode;
    private long purchasePrice;
    private LocalDate purchaseDt;
}
