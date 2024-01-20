package com.habibi.stockstoryapi.repository;


import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockSellRecordRepository extends JpaRepository<StockSellRecordEntity, Long> {
    List<StockSellRecordEntity> findAllByUserId(int userid);
    List<StockSellRecordEntity> findAllByUserIdAndStockCode(int userid, String stockCode);
    List<StockSellRecordEntity> findAllByUserIdAndStoryId(int userid, long storyId);
    List<StockSellRecordEntity> findAllByUserIdAndSellDtIsBefore(int userid, LocalDate date);
    List<StockSellRecordEntity> findAllByUserIdAndSellDtIsBetween(int userid, LocalDate start, LocalDate end);
}
