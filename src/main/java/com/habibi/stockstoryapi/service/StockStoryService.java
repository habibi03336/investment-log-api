package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;

import java.util.List;

public interface StockStoryService {
    CreateStatusDto createStockStory(int userId, StockStoryDto stockStoryDto);
    CreateStatusDto createLongPositionStory(int userId, StockStoryDto stockStoryDto);
    CreateStatusDto createShortPositionStory(int userId, StockStoryDto stockStoryDto);
    StockStoryDto readStockStoryById(long storyId);
    List<StockStoryDto> readStockStoryOfCertainStock(int userId, String stockCode);
    List<StockStoryDto> readStockLongPositionStoryOfCertainStock(int userId, String stockCode);
    List<StockStoryDto> readStockShortPositionStoryOfCertainStock(int userId, String stockCode);
}
