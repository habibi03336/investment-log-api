package com.habibi.stockstoryapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "storyId")
@Entity
public class StockStoryEntity {
    @Id @GeneratedValue
    private long storyId;
    private String positionType;
    private int userId;
    private LocalDateTime creationDt;
    private String story;
}
