package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockPositionStoryDto;

import java.util.List;

public interface StockPositionStoryService {
    CreateStatusDto createPurchaseStory(StockPositionStoryDto stockPositionStoryDto);
    CreateStatusDto createSellStory(StockPositionStoryDto stockPositionStoryDto);
    List<StockPositionStoryDto> readStockLongPositionStoryOfCertainStock(String stockCode);
    List<StockPositionStoryDto> readStockShortPositionStoryOfCertainStock(String stockCode);
}
