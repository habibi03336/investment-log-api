package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;

import java.util.List;

public interface StockStoryService {
    CreateStatusDto createPurchaseStory(StockStoryDto stockStoryDto);
    CreateStatusDto createSellStory(StockStoryDto stockStoryDto);
    List<StockStoryDto> readPurchaseStoryOfCertainStock(String stockCode);
    List<StockStoryDto> readSellStoryOfCertainStock(String stockCode);
}
