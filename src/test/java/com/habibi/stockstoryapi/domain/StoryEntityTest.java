package com.habibi.stockstoryapi.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class StoryEntityTest {

    @Test
    public void builder(){
        StoryEntity storyEntity = StoryEntity.builder().build();
        assertThat(storyEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String content = "I think the semiconductor market will grow rapidly";
        LocalDateTime creationDt = LocalDateTime.of(LocalDate.of(2023,9, 26), LocalTime.of(22, 15, 0, 0));

        // When
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setContent(content);
        storyEntity.setCreationDt(creationDt);

        // Then
        assertThat(storyEntity.getContent()).isEqualTo(content);
        assertThat(storyEntity.getCreationDt()).isEqualTo(creationDt);
    }
}