package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;

import java.util.List;

public interface StockStoryService {
    CreateStatusDto createStockStory(StockStoryDto stockStoryDto);
    CreateStatusDto createLongPositionStory(StockStoryDto stockStoryDto);
    CreateStatusDto createShortPositionStory(StockStoryDto stockStoryDto);
    StockStoryDto readStockStoryById(long id);
    List<StockStoryDto> readStockStoryOfCertainStock(String stockCode);
    List<StockStoryDto> readStockLongPositionStoryOfCertainStock(String stockCode);
    List<StockStoryDto> readStockShortPositionStoryOfCertainStock(String stockCode);
}
