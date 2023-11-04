package com.habibi.stockstoryapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class StockPositionStoryEntityTest {

    @Test
    public void builder(){
        StockPositionStoryEntity stockPositionStoryEntity = StockPositionStoryEntity.builder().build();
        assertThat(stockPositionStoryEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String story = "I think the semiconductor market will grow rapidly";
        LocalDateTime creationDt = LocalDateTime.of(LocalDate.of(2023,9, 26), LocalTime.of(22, 15, 0, 0));

        // When
        StockPositionStoryEntity stockPositionStoryEntity = new StockPositionStoryEntity();
        stockPositionStoryEntity.setStory(story);
        stockPositionStoryEntity.setCreationDt(creationDt);

        // Then
        assertThat(stockPositionStoryEntity.getStory()).isEqualTo(story);
        assertThat(stockPositionStoryEntity.getCreationDt()).isEqualTo(creationDt);
    }
}