package com.habibi.stockstoryapi.repository;


import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockSellRecordRepository extends JpaRepository<StockSellRecordEntity, Long> {

    List<StockSellRecordEntity> findAllByStockCode(String stockCode);
    List<StockSellRecordEntity> findAllByStoryId(long storyId);
    List<StockSellRecordEntity> findAllBySellDtIsBefore(LocalDate date);
    List<StockSellRecordEntity> findAllBySellDtIsBetween(LocalDate start, LocalDate end);
}
