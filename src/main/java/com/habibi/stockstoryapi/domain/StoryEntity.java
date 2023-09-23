package com.habibi.stockstoryapi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "storyId")
public class StoryEntity {
    private long storyId;
    private long userId;
    private LocalDateTime creationDt;
    private String content;
}
