package com.habibi.stockstoryapi.repository;


import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockPurchaseRecordRepository extends JpaRepository<StockPurchaseRecordEntity, Long> {
    List<StockPurchaseRecordEntity> findAllByUserId(int userid);
    List<StockPurchaseRecordEntity> findAllByUserIdAndStockCode(int userid, String stockCode);
    List<StockPurchaseRecordEntity> findAllByStoryId(long storyId);
    List<StockPurchaseRecordEntity> findAllByUserIdAndPurchaseDtIsBefore(int userid, LocalDate date);
    List<StockPurchaseRecordEntity> findAllByUserIdAndPurchaseDtIsBetween(int userid, LocalDate start, LocalDate end);
}
