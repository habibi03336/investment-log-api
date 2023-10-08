package com.habibi.stockstoryapi.repository;

import com.habibi.stockstoryapi.domain.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<StoryEntity, Long> {
}
