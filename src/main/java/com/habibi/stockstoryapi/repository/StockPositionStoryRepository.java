package com.habibi.stockstoryapi.repository;

import com.habibi.stockstoryapi.domain.StockStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPositionStoryRepository extends JpaRepository<StockStoryEntity, Long> {
    StockStoryEntity findByStoryId(long storyId);
}
