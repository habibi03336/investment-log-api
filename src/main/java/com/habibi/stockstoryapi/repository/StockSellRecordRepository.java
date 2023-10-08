package com.habibi.stockstoryapi.repository;

import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSellRecordRepository extends JpaRepository<StockSellRecordEntity, Long> {
}
