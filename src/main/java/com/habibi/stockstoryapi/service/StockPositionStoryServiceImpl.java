package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockPositionStoryEntity;
import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockPositionStoryDto;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;

import java.time.LocalDate;
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
        String stockCode = stockPositionStoryDto.getStockCode();
        int[] stockPrices = stockPositionStoryDto.getStockPrices();
        LocalDate date = stockPositionStoryDto.getDt();
        String story = stockPositionStoryDto.getStory();
        StockPositionStoryEntity stockPositionStoryEntity = StockPositionStoryEntity
                .builder()
                .positionType("Long")
                .story(story)
                .build();
        stockPositionStoryRepository.save(stockPositionStoryEntity);
        Long storyId = stockPositionStoryEntity.getStoryId();
        for(int price : stockPrices){
            StockPurchaseRecordEntity stockPurchaseRecordEntity = StockPurchaseRecordEntity
                    .builder()
                    .storyId(storyId)
                    .stockCode(stockCode)
                    .purchaseDt(date)
                    .purchasePrice(price)
                    .build();
            stockPurchaseRecordRepository.save(stockPurchaseRecordEntity);
        }
        return CreateStatusDto.builder().status(CreateStatusDto.Status.SUCCESS).build();
    }

    @Override
    public CreateStatusDto createShortPositionStory(StockPositionStoryDto stockPositionStoryDto) {
        String stockCode = stockPositionStoryDto.getStockCode();
        int[] stockPrices = stockPositionStoryDto.getStockPrices();
        LocalDate date = stockPositionStoryDto.getDt();
        String story = stockPositionStoryDto.getStory();
        StockPositionStoryEntity stockPositionStoryEntity = StockPositionStoryEntity
                .builder()
                .positionType("Short")
                .story(story)
                .build();
        stockPositionStoryRepository.save(stockPositionStoryEntity);
        Long storyId = stockPositionStoryEntity.getStoryId();
        List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByStockCode(stockCode);
        List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllByStockCode(stockCode);
        long[] countAndTotalPrices = new long[]{0, 0};
        for(StockPurchaseRecordEntity stockPurchaseRecordEntity: stockPurchaseRecordEntities){
            countAndTotalPrices[0] += 1;
            countAndTotalPrices[1] += stockPurchaseRecordEntity.getPurchasePrice();
        }
        for(StockSellRecordEntity stockSellRecordEntity: stockSellRecordEntities){
            countAndTotalPrices[0] -= 1;
            countAndTotalPrices[1] -= stockSellRecordEntity.getAvgPurchasePrice();
        }
        int averagePurchasePrice = (int)(countAndTotalPrices[1]/countAndTotalPrices[0]);
        for(int price : stockPrices){
            StockSellRecordEntity stockSellRecordEntity = StockSellRecordEntity
                    .builder()
                    .storyId(storyId)
                    .stockCode(stockCode)
                    .sellDt(date)
                    .avgPurchasePrice(averagePurchasePrice)
                    .sellPrice(price)
                    .build();
            stockSellRecordRepository.save(stockSellRecordEntity);
        }
        return CreateStatusDto.builder().status(CreateStatusDto.Status.SUCCESS).build();
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
                            .story(stockPositionStoryEntity.getStory())
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
                            .story(stockPositionStoryEntity.getStory())
                            .build()
            );
        }
        return stockPositionStoryDtos.stream().sorted(Comparator.comparing(StockPositionStoryDto::getDt, Comparator.reverseOrder())).collect(Collectors.toList());
    }
}
