package com.habibi.stockstoryapi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "stockPurchaseId")
public class StockPurchaseRecordEntity {
    private long stockPurchaseId;
    private long storyId;
    private long userId;
    private String stockCode;
    private long purchasePrice;
    private LocalDateTime purchaseDt;
}
