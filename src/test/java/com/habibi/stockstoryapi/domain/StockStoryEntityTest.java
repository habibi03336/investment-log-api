package com.habibi.stockstoryapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class StockStoryEntityTest {

    @Test
    public void builder(){
        StockStoryEntity stockStoryEntity = StockStoryEntity.builder().build();
        assertThat(stockStoryEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String story = "I think the semiconductor market will grow rapidly";
        LocalDateTime creationDt = LocalDateTime.of(LocalDate.of(2023,9, 26), LocalTime.of(22, 15, 0, 0));

        // When
        StockStoryEntity stockStoryEntity = new StockStoryEntity();
        stockStoryEntity.setStory(story);
        stockStoryEntity.setCreationDt(creationDt);

        // Then
        assertThat(stockStoryEntity.getStory()).isEqualTo(story);
        assertThat(stockStoryEntity.getCreationDt()).isEqualTo(creationDt);
    }
}