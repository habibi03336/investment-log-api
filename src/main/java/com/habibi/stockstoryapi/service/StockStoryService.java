package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockPositionStoryDto;

import java.util.List;

public interface StockStoryService {
    CreateStatusDto createPurchaseStory(StockPositionStoryDto stockPositionStoryDto);
    CreateStatusDto createSellStory(StockPositionStoryDto stockPositionStoryDto);
    List<StockPositionStoryDto> readPurchaseStoryOfCertainStock(String stockCode);
    List<StockPositionStoryDto> readSellStoryOfCertainStock(String stockCode);
}
