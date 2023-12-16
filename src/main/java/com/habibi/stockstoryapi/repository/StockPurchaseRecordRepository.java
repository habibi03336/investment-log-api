package com.habibi.stockstoryapi.repository;


import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockPurchaseRecordRepository extends JpaRepository<StockPurchaseRecordEntity, Long> {

    List<StockPurchaseRecordEntity> findAllByStockCode(String stockCode);
    List<StockPurchaseRecordEntity> findAllByStoryId(long storyId);
    List<StockPurchaseRecordEntity> findAllByPurchaseDtIsBefore(LocalDate date);
    List<StockPurchaseRecordEntity> findAllByPurchaseDtIsBetween(LocalDate start, LocalDate end);
}
