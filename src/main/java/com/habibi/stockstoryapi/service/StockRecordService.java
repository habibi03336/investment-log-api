package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.StockRecordDto;

import java.time.LocalDate;
import java.util.List;

public interface StockRecordService {
    List<StockRecordDto> readStockPurchaseRecordsBetweenPeriods(LocalDate start, LocalDate end);
    List<StockRecordDto> readStockSellRecordsBetweenPeriods(LocalDate start, LocalDate end);
}
