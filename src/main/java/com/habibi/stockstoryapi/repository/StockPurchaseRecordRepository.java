package com.habibi.stockstoryapi.repository;


import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPurchaseRecordRepository extends JpaRepository<StockPurchaseRecordEntity, Long> {
}
