package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPositionStoryEntity;
import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockPositionStoryDto;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;

import java.util.*;
import java.util.stream.Collectors;

public class StockPositionStoryServiceImpl implements  StockPositionStoryService {
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private StockPositionStoryRepository stockPositionStoryRepository;
    public StockPositionStoryServiceImpl(
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository,
            StockPositionStoryRepository stockPositionStoryRepository
    ){
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.stockPositionStoryRepository = stockPositionStoryRepository;
    }
    @Override
    public CreateStatusDto createLongPositionStory(StockPositionStoryDto stockPositionStoryDto) {
        return null;
    }

    @Override
    public CreateStatusDto createShortPositionStory(StockPositionStoryDto stockPositionStoryDto) {
        return null;
    }

    @Override
    public List<StockPositionStoryDto> readStockLongPositionStoryOfCertainStock(String stockCode) {
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByStockCode(stockCode);
        Map<Long, List<StockPurchaseRecordEntity>> stockPurchaseRecordEntitiesByStoryId = new HashMap<>();
        for(StockPurchaseRecordEntity stockPurchaseRecordEntity : stockPurchaseRecordEntities){
            long storyId = stockPurchaseRecordEntity.getStoryId();
            if(stockPurchaseRecordEntitiesByStoryId.containsKey(storyId)){
                List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = stockPurchaseRecordEntitiesByStoryId.get(storyId);
                stockPurchaseRecordEntitiesWithCertainStoryId.add(stockPurchaseRecordEntity);
            } else {
                List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = new ArrayList<>();
                stockPurchaseRecordEntitiesWithCertainStoryId.add(stockPurchaseRecordEntity);
                stockPurchaseRecordEntitiesByStoryId.put(storyId, stockPurchaseRecordEntitiesWithCertainStoryId);
            }
        }
        List<StockPositionStoryDto> stockPositionStoryDtos = new ArrayList<>();
        for(long storyId: stockPurchaseRecordEntitiesByStoryId.keySet()){
            List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = stockPurchaseRecordEntitiesByStoryId.get(storyId);
            StockPositionStoryEntity stockPositionStoryEntity = stockPositionStoryRepository.findByStoryId(storyId);
            stockPositionStoryDtos.add(
                    StockPositionStoryDto
                            .builder()
                            .dt(stockPurchaseRecordEntitiesWithCertainStoryId.get(0).getPurchaseDt())
                            .stockCode(stockPurchaseRecordEntitiesWithCertainStoryId.get(0).getStockCode())
                            .stockPrices(
                                    stockPurchaseRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getPurchasePrice())
                                            .toArray()
                            )
                            .story(stockPositionStoryEntity.getContent())
                            .build()
            );
        }
        return stockPositionStoryDtos.stream().sorted(Comparator.comparing(StockPositionStoryDto::getDt, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<StockPositionStoryDto> readStockShortPositionStoryOfCertainStock(String stockCode) {
        List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllByStockCode(stockCode);
        Map<Long, List<StockSellRecordEntity>> stockSellRecordEntitiesByStoryId = new HashMap<>();
        for(StockSellRecordEntity stockSellRecordEntity : stockSellRecordEntities){
            long storyId = stockSellRecordEntity.getStoryId();
            if(stockSellRecordEntitiesByStoryId.containsKey(storyId)){
                List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = stockSellRecordEntitiesByStoryId.get(storyId);
                stockSellRecordEntitiesWithCertainStoryId.add(stockSellRecordEntity);
            } else {
                List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = new ArrayList<>();
                stockSellRecordEntitiesWithCertainStoryId.add(stockSellRecordEntity);
                stockSellRecordEntitiesByStoryId.put(storyId, stockSellRecordEntitiesWithCertainStoryId);
            }
        }
        List<StockPositionStoryDto> stockPositionStoryDtos = new ArrayList<>();
        for(long storyId: stockSellRecordEntitiesByStoryId.keySet()){
            List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = stockSellRecordEntitiesByStoryId.get(storyId);
            StockPositionStoryEntity stockPositionStoryEntity = stockPositionStoryRepository.findByStoryId(storyId);
            stockPositionStoryDtos.add(
                    StockPositionStoryDto
                            .builder()
                            .dt(stockSellRecordEntitiesWithCertainStoryId.get(0).getSellDt())
                            .stockCode(stockSellRecordEntitiesWithCertainStoryId.get(0).getStockCode())
                            .stockPrices(
                                    stockSellRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getSellPrice())
                                            .toArray()
                            )
                            .story(stockPositionStoryEntity.getContent())
                            .build()
            );
        }
        return stockPositionStoryDtos.stream().sorted(Comparator.comparing(StockPositionStoryDto::getDt, Comparator.reverseOrder())).collect(Collectors.toList());
    }
}
