package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.StockStoryEntity;
import com.habibi.stockstoryapi.domain.StockPurchaseRecordEntity;
import com.habibi.stockstoryapi.domain.StockSellRecordEntity;
import com.habibi.stockstoryapi.dto.CreateStatusDto;
import com.habibi.stockstoryapi.dto.StockStoryDto;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockStoryServiceImpl implements StockStoryService {
    private StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private StockSellRecordRepository stockSellRecordRepository;
    private StockPositionStoryRepository stockPositionStoryRepository;
    public StockStoryServiceImpl(
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository,
            StockPositionStoryRepository stockPositionStoryRepository
    ){
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.stockPositionStoryRepository = stockPositionStoryRepository;
    }

    @Override
    public CreateStatusDto createStockStory(StockStoryDto stockStoryDto) {
        if(stockStoryDto.isLong()){
            return createLongPositionStory(stockStoryDto);
        } else {
            return createShortPositionStory(stockStoryDto);
        }
    }

    @Override
    public CreateStatusDto createLongPositionStory(StockStoryDto stockStoryDto) {
        String stockCode = stockStoryDto.getStockCode();
        int[] stockPrices = stockStoryDto.getStockPrices();
        LocalDate date = stockStoryDto.getDt();
        String story = stockStoryDto.getStory();
        StockStoryEntity stockStoryEntity = StockStoryEntity
                .builder()
                .positionType("Long")
                .story(story)
                .build();
        stockPositionStoryRepository.save(stockStoryEntity);
        Long storyId = stockStoryEntity.getStoryId();
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
    public CreateStatusDto createShortPositionStory(StockStoryDto stockStoryDto) {
        String stockCode = stockStoryDto.getStockCode();
        int[] stockPrices = stockStoryDto.getStockPrices();
        LocalDate date = stockStoryDto.getDt();
        String story = stockStoryDto.getStory();
        StockStoryEntity stockStoryEntity = StockStoryEntity
                .builder()
                .positionType("Short")
                .story(story)
                .build();
        stockPositionStoryRepository.save(stockStoryEntity);
        Long storyId = stockStoryEntity.getStoryId();
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
    public StockStoryDto readStockStoryById(long id) {
        StockStoryEntity stockStoryEntity = stockPositionStoryRepository.findByStoryId(id);
        if(stockStoryEntity.getPositionType().equals("Long")){
            List<StockPurchaseRecordEntity> stockPurchaseRecordEntities = stockPurchaseRecordRepository.findAllByStoryId(id);
            return StockStoryDto.builder()
                    .storyId(id)
                    .stockCode(stockPurchaseRecordEntities.get(0).getStockCode())
                    .stockPrices(stockPurchaseRecordEntities.stream().mapToInt(StockPurchaseRecordEntity::getPurchasePrice).toArray())
                    .isLong(true)
                    .dt(stockPurchaseRecordEntities.get(0).getPurchaseDt())
                    .story(stockStoryEntity.getStory())
                    .build();
        } else {
            List<StockSellRecordEntity> stockSellRecordEntities = stockSellRecordRepository.findAllByStoryId(id);
            return StockStoryDto.builder()
                    .storyId(id)
                    .stockCode(stockSellRecordEntities.get(0).getStockCode())
                    .stockPrices(stockSellRecordEntities.stream().mapToInt(StockSellRecordEntity::getSellPrice).toArray())
                    .averagePurchasePrice(
                                stockSellRecordEntities.stream().mapToInt(StockSellRecordEntity::getAvgPurchasePrice).sum()
                                        /
                                stockSellRecordEntities.size()
                            )
                    .isLong(false)
                    .dt(stockSellRecordEntities.get(0).getSellDt())
                    .story(stockStoryEntity.getStory())
                    .build();
        }
    }


    @Override
    public List<StockStoryDto> readStockLongPositionStoryOfCertainStock(String stockCode) {
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
        List<StockStoryDto> stockStoryDtos = new ArrayList<>();
        for(long storyId: stockPurchaseRecordEntitiesByStoryId.keySet()){
            List<StockPurchaseRecordEntity> stockPurchaseRecordEntitiesWithCertainStoryId = stockPurchaseRecordEntitiesByStoryId.get(storyId);
            StockStoryEntity stockStoryEntity = stockPositionStoryRepository.findByStoryId(storyId);
            stockStoryDtos.add(
                    StockStoryDto
                            .builder()
                            .dt(stockPurchaseRecordEntitiesWithCertainStoryId.get(0).getPurchaseDt())
                            .stockCode(stockPurchaseRecordEntitiesWithCertainStoryId.get(0).getStockCode())
                            .stockPrices(
                                    stockPurchaseRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getPurchasePrice())
                                            .toArray()
                            )
                            .story(stockStoryEntity.getStory())
                            .build()
            );
        }
        return stockStoryDtos.stream().sorted(Comparator.comparing(StockStoryDto::getDt, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<StockStoryDto> readStockShortPositionStoryOfCertainStock(String stockCode) {
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
        List<StockStoryDto> stockStoryDtos = new ArrayList<>();
        for(long storyId: stockSellRecordEntitiesByStoryId.keySet()){
            List<StockSellRecordEntity> stockSellRecordEntitiesWithCertainStoryId = stockSellRecordEntitiesByStoryId.get(storyId);
            StockStoryEntity stockStoryEntity = stockPositionStoryRepository.findByStoryId(storyId);
            stockStoryDtos.add(
                    StockStoryDto
                            .builder()
                            .dt(stockSellRecordEntitiesWithCertainStoryId.get(0).getSellDt())
                            .stockCode(stockSellRecordEntitiesWithCertainStoryId.get(0).getStockCode())
                            .stockPrices(
                                    stockSellRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getSellPrice())
                                            .toArray()
                            )
                            .averagePurchasePrice(
                                    (
                                            stockSellRecordEntitiesWithCertainStoryId
                                            .stream()
                                            .mapToInt(entity -> entity.getAvgPurchasePrice())
                                            .sum()
                                                    /
                                            stockSellRecordEntitiesWithCertainStoryId.size()
                                    )
                            )
                            .story(stockStoryEntity.getStory())
                            .build()
            );
        }
        return stockStoryDtos.stream().sorted(Comparator.comparing(StockStoryDto::getDt, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<StockStoryDto> readStockStoryOfCertainStock(String stockCode){
        List<StockStoryDto> stories = Stream.concat(
                readStockShortPositionStoryOfCertainStock(stockCode).stream(),
                readStockLongPositionStoryOfCertainStock(stockCode).stream()
        ).toList();
        stories.sort(Comparator.comparing(StockStoryDto::getDt));
        return stories;
    }
}
