package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.StockRecordDto;
import com.habibi.stockstoryapi.dto.StockStateDto;

public interface StockRecordKeeper {
    StockStateDto getCurrentOwnStockState(int userId, String stockCode);
    void saveStockSellRecord(StockRecordDto stockRecordDto);
    void saveStockPurchaseRecord(StockRecordDto stockRecordDto);

}
