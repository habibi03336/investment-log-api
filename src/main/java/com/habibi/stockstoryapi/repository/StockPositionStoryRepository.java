package com.habibi.stockstoryapi.repository;

import com.habibi.stockstoryapi.domain.StockPositionStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPositionStoryRepository extends JpaRepository<StockPositionStoryEntity, Long> {
    StockPositionStoryEntity findByStoryId(long storyId);
}
