package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.StockRecordDto;

import java.time.LocalDate;
import java.util.List;

public interface StockRecordService {
    List<StockRecordDto> readStockPurchaseRecordsBetweenPeriods(int userId, LocalDate start, LocalDate end);
    List<StockRecordDto> readStockSellRecordsBetweenPeriods(int userId, LocalDate start, LocalDate end);
}
